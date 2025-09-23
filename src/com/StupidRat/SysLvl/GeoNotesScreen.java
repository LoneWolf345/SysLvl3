package com.StupidRat.SysLvl;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.StupidRat.SysLvl.legacy.LegacyServiceLocator;
import com.StupidRat.SysLvl.legacy.data.GeoNote;
import com.StupidRat.SysLvl.legacy.data.GeoNoteRepository;

public class GeoNotesScreen extends ListActivity {


        private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
        private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
        private static final int REQUEST_LOCATION_PERMISSION = 1001;

        protected LocationManager locationManager;
        protected Button retrieveLocationButton;
        private View retrieveLocationProgressChip;
        private LocationListener locationListener;
    private ExecutorService geocodingExecutor;
    private Future<?> pendingGeocodingTask;

    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;

    private GeoNoteRepository geoNoteRepository;
    private final ArrayList<GeoNote> notes = new ArrayList<GeoNote>();
    private final ArrayList<String> noteTitles = new ArrayList<String>();
    private ArrayAdapter<String> notesAdapter;
    private final GeoNoteRepository.Observer noteObserver = new GeoNoteRepository.Observer() {
        @Override
        public void onNotesChanged(List<GeoNote> updatedNotes) {
            notes.clear();
            noteTitles.clear();
            if (updatedNotes != null) {
                notes.addAll(updatedNotes);
                for (GeoNote note : updatedNotes) {
                    noteTitles.add(note.getTitle());
                }
            }
            if (notesAdapter != null) {
                notesAdapter.notifyDataSetChanged();
            }
        }
    };

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geonotes);

        notesAdapter = new ArrayAdapter<String>(this, R.layout.notes_row, R.id.text1, noteTitles);
        setListAdapter(notesAdapter);

        try {
            geoNoteRepository = LegacyServiceLocator.provideGeoNoteRepository(this);
            geoNoteRepository.addObserver(noteObserver);
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to load notes", Toast.LENGTH_LONG).show();
        }

        fillData();
        registerForContextMenu(getListView());

        // Create add note button listener
        Button btnAddNote = (Button) findViewById(R.id.ButtonAddNote);
                btnAddNote.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View view) {
                                createNote();
                        }
                });


                //Location
        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        retrieveLocationProgressChip = findViewById(R.id.retrieve_location_progress_chip);

        geocodingExecutor = Executors.newSingleThreadExecutor();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new MyLocationListener();

        updateRetrieveLocationButtonState(hasLocationPermission());

                retrieveLocationButton.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                if (hasLocationPermission()) {
                                        showCurrentLocation();
                                } else {
                                        handleMissingLocationPermission();
                                        ActivityCompat.requestPermissions(
                                                        GeoNotesScreen.this,
                                                        new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                                                        REQUEST_LOCATION_PERMISSION);
                                }
                        }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLocationUpdatesIfPermitted();
        fillData();
    }

    @Override
    protected void onPause() {
        stopLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cancelPendingGeocodingTask();
        setRetrievingLocationInProgress(false);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        stopLocationUpdates();
        cancelPendingGeocodingTask();
        if (geocodingExecutor != null) {
            geocodingExecutor.shutdownNow();
            geocodingExecutor = null;
        }
        if (geoNoteRepository != null) {
            geoNoteRepository.removeObserver(noteObserver);
            geoNoteRepository.close();
        }
        super.onDestroy();
    }

        protected void showCurrentLocation() {

                if (!hasLocationPermission()) {
                        handleMissingLocationPermission();
                        return;
                }

                final Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location == null) {
                        Toast.makeText(this, "Current location unavailable", Toast.LENGTH_SHORT).show();
                        return;
                }

                if (geocodingExecutor == null || geocodingExecutor.isShutdown()) {
                        geocodingExecutor = Executors.newSingleThreadExecutor();
                }

                final double latitude = location.getLatitude();
                final double longitude = location.getLongitude();
                final float speed = location.getSpeed();
                final float accuracy = location.getAccuracy();
                final String latitudeString = String.valueOf(latitude);
                final String longitudeString = String.valueOf(longitude);

                setRetrievingLocationInProgress(true);
                cancelPendingGeocodingTask();

                try {
                        pendingGeocodingTask = geocodingExecutor.submit(new Runnable() {
                                @Override
                                public void run() {
                                    Geocoder geocoder = new Geocoder(GeoNotesScreen.this, Locale.getDefault());
                                    String street = null;
                                    String state = null;
                                    String zip = null;

                                try {
                                        List<Address> addresses = geocoder.getFromLocation(latitude,
                                                        longitude, 1);
                                        if (addresses != null && !addresses.isEmpty()) {
                                                Address address = addresses.get(0);
                                                street = address.getThoroughfare();
                                                if (street == null) {
                                                        street = address.getFeatureName();
                                                }
                                                state = address.getAdminArea();
                                                zip = address.getPostalCode();
                                        }
                                } catch (IOException e) {
                                        Log.w("GeoNotesScreen", "Reverse geocoding failed", e);
                                }

                                if (Thread.currentThread().isInterrupted()) {
                                        return;
                                }

                                final String finalStreet = street;
                                final String finalState = state;
                                final String finalZip = zip;
                                final String message = String.format("Longitude: %1$s \n Latitude: %2$s",
                                                longitude, latitude);
                                final String body = String.format("Location \n Longitude: %1$s \n Latitude: %2$s \n Speed: %3$s \n Acuracy: %4$s",
                                                longitude, latitude, speed, accuracy);

                                        runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                        pendingGeocodingTask = null;
                                                        if (isFinishing()) {
                                                                setRetrievingLocationInProgress(false);
                                                                return;
                                                        }

                                                TextView tvLocation = (TextView) findViewById(R.id.TextViewLocationAcuracy);
                                                if (tvLocation != null) {
                                                        tvLocation.setText("Accuracy: " + String.valueOf(accuracy));
                                                }

                                                if (geoNoteRepository != null) {
                                                        try {
                                                                geoNoteRepository.createNote(message, body, latitudeString, longitudeString, finalStreet, finalState, finalZip);
                                                        } catch (SQLException e) {
                                                                Toast.makeText(GeoNotesScreen.this, "Unable to save note", Toast.LENGTH_SHORT).show();
                                                        }
                                                }
                                                fillData();
                                                setRetrievingLocationInProgress(false);
                                        }
                                        });
                                }
                        });
                } catch (RejectedExecutionException e) {
                        Log.w("GeoNotesScreen", "Unable to start geocoding task", e);
                        setRetrievingLocationInProgress(false);
                }

        }

    private void setRetrievingLocationInProgress(boolean inProgress) {
        if (retrieveLocationButton != null) {
            if (inProgress) {
                retrieveLocationButton.setEnabled(false);
            } else {
                updateRetrieveLocationButtonState(hasLocationPermission());
            }
        }
        if (retrieveLocationProgressChip != null) {
            retrieveLocationProgressChip.setVisibility(inProgress ? View.VISIBLE : View.GONE);
        }
    }

    private void cancelPendingGeocodingTask() {
        if (pendingGeocodingTask != null) {
            pendingGeocodingTask.cancel(true);
            pendingGeocodingTask = null;
        }
    }

        private class MyLocationListener implements LocationListener {

                public void onLocationChanged(Location location) {
                        TextView tvLocation = (TextView) findViewById(R.id.TextViewLocationAcuracy);

                        String message = String.format("New Location \n Longitude: %1$s \n Latitude: %2$s \n Accuracy: %3$s",
                                        location.getLongitude(), location.getLatitude(), location.getAccuracy());
                        tvLocation.setText("Accuracy: "+ String.valueOf(location.getAccuracy()));
                        //Toast.makeText(GeoNotes.this, message, Toast.LENGTH_LONG).show();
                }

                public void onStatusChanged(String s, int i, Bundle b) {
                        //Toast.makeText(GeoNotes.this, "Provider status changed",
                        //              Toast.LENGTH_LONG).show();
                }

                public void onProviderDisabled(String s) {
                        //Toast.makeText(GeoNotes.this,
                        //              "Provider disabled by the user. GPS turned off",
                        //              Toast.LENGTH_LONG).show();
                }

                public void onProviderEnabled(String s) {
                        Toast.makeText(GeoNotesScreen.this,
                                        "Provider enabled by the user. GPS turned on",
                                        Toast.LENGTH_LONG).show();
                }

        }

    private void requestLocationUpdatesIfPermitted() {
        if (locationManager == null || locationListener == null) {
            return;
        }

        if (hasLocationPermission()) {
            updateRetrieveLocationButtonState(true);
            startLocationUpdates();
        } else {
            handleMissingLocationPermission();
            ActivityCompat.requestPermissions(
                    this,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private void startLocationUpdates() {
        if (locationManager == null || locationListener == null || !hasLocationPermission()) {
            return;
        }

        try {
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MINIMUM_TIME_BETWEEN_UPDATES,
                    MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                    locationListener
            );
        } catch (SecurityException ignored) {
            handleMissingLocationPermission();
        }
    }

    private void stopLocationUpdates() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
    }

    private boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateRetrieveLocationButtonState(boolean enabled) {
        if (retrieveLocationButton != null) {
            retrieveLocationButton.setEnabled(enabled);
            if (!enabled) {
                retrieveLocationButton.setContentDescription(getString(R.string.location_permission_required_message));
            } else {
                retrieveLocationButton.setContentDescription(null);
            }
        }
    }

    private void handleMissingLocationPermission() {
        updateRetrieveLocationButtonState(false);
        TextView tvLocation = (TextView) findViewById(R.id.TextViewLocationAcuracy);
        if (tvLocation != null) {
            tvLocation.setText(R.string.location_permission_required_message);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults != null && grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateRetrieveLocationButtonState(true);
                startLocationUpdates();
            } else {
                handleMissingLocationPermission();
                Toast.makeText(this, R.string.location_permission_denied_message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void fillData() {
        if (geoNoteRepository != null) {
            try {
                geoNoteRepository.refresh();
            } catch (SQLException e) {
                Toast.makeText(this, "Unable to load notes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }

    @Override
        public void onCreateContextMenu(ContextMenu menu, View v,
                        ContextMenuInfo menuInfo) {
                super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
        }

    @Override
        public boolean onContextItemSelected(MenuItem item) {
                switch(item.getItemId()) {
        case DELETE_ID:
                AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
                if (geoNoteRepository != null) {
                    try {
                        geoNoteRepository.deleteNote(info.id);
                    } catch (SQLException e) {
                        Toast.makeText(this, "Unable to delete note", Toast.LENGTH_SHORT).show();
                    }
                }
                fillData();
                return true;
                }
                return super.onContextItemSelected(item);
        }

    private void createNote() {
        Intent i = new Intent(this, GeoNoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (position >= 0 && position < notes.size()) {
            GeoNote note = notes.get(position);
            Intent i = new Intent(this, GeoNoteEdit.class);
            i.putExtra(GeoNotesDbAdapter.KEY_ROWID, note.getId());
            startActivityForResult(i, ACTIVITY_EDIT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}
