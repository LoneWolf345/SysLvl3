package com.StupidRat.SysLvl;

import java.sql.SQLException;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.StupidRat.SysLvl.legacy.LegacyServiceLocator;
import com.StupidRat.SysLvl.legacy.data.GeoNote;
import com.StupidRat.SysLvl.legacy.data.GeoNoteRepository;

public class GeoNoteEdit extends SysLvlActivity {

        private EditText mTitleText;
    private EditText mBodyText;
    private EditText mLatText;
    private EditText mLongText;
    private EditText mStreetText;
    private EditText mStateText;
    private EditText mZipText;
    private Long mRowId;
    private GeoNoteRepository geoNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            geoNoteRepository = LegacyServiceLocator.provideGeoNoteRepository(this);
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to open notes", Toast.LENGTH_LONG).show();
        }
        setContentView(R.layout.note_edit);


        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        mLatText = (EditText) findViewById(R.id.etLat);
        mLongText = (EditText) findViewById(R.id.etLong);
        mStreetText = (EditText) findViewById(R.id.etStreet);
        mStateText = (EditText) findViewById(R.id.etState);
        mZipText = (EditText) findViewById(R.id.etZip);

        Button confirmButton = (Button) findViewById(R.id.confirm);

        mRowId = savedInstanceState != null ? savedInstanceState.getLong(GeoNotesDbAdapter.KEY_ROWID)
                                                                        : null;
                if (mRowId == null) {
                        Bundle extras = getIntent().getExtras();
                        mRowId = extras != null ? extras.getLong(GeoNotesDbAdapter.KEY_ROWID)
                                                                        : null;
                }

                populateFields();

        confirmButton.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    setResult(RESULT_OK);
                    finish();
                }

        });
    }

    private void populateFields() {
        if (mRowId != null && geoNoteRepository != null) {
            try {
                GeoNote note = geoNoteRepository.getNote(mRowId);
                if (note != null) {
                    mTitleText.setText(note.getTitle());
                    mBodyText.setText(note.getBody());
                    mLatText.setText(note.getLatitude());
                    mLongText.setText(note.getLongitude());
                    mStreetText.setText(note.getStreet());
                    mStateText.setText(note.getState());
                    mZipText.setText(note.getZip());
                }
            } catch (SQLException e) {
                Toast.makeText(this, "Unable to load note", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(GeoNotesDbAdapter.KEY_ROWID, mRowId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    private void saveState() {
        String title = mTitleText.getText().toString();
        String body = mBodyText.getText().toString();
        String latitude = mLatText.getText().toString();
        String longitude = mLongText.getText().toString();
        String street = mStreetText.getText().toString();
        String state = mStateText.getText().toString();
        String zip = mZipText.getText().toString();

        if (geoNoteRepository == null) {
            return;
        }

        try {
            if (mRowId == null) {
                long id = geoNoteRepository.createNote(title, body, latitude, longitude, street, state, zip);
                if (id > 0) {
                    mRowId = id;
                }
            } else {
                geoNoteRepository.updateNote(mRowId, title, body, latitude, longitude, street, state, zip);
            }
        } catch (SQLException e) {
            Toast.makeText(this, "Unable to save note", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (geoNoteRepository != null) {
            geoNoteRepository.close();
        }
        super.onDestroy();
    }

}
