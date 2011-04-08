package com.StupidRat.SysLvl;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GeoNoteEdit extends SysLvlActivity {

	private EditText mTitleText;
    private EditText mBodyText;
    private EditText mLatText;
    private EditText mLongText;
    private EditText mStreetText;
    private EditText mStateText;
    private EditText mZipText;
    private Long mRowId;
    private GeoNotesDbAdapter mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new GeoNotesDbAdapter(this);
        mDbHelper.open();
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
        if (mRowId != null) {
            Cursor note = mDbHelper.fetchNote(mRowId);
            startManagingCursor(note);
            mTitleText.setText(note.getString(
    	            note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_TITLE)));
            mBodyText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_BODY)));
            mLatText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LAT)));
            mLongText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LONG)));
            mStreetText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STREET)));
            mStateText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STATE)));
            mZipText.setText(note.getString(
                    note.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_ZIP)));
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

        if (mRowId == null) {
            long id = mDbHelper.createNote(title, body, latitude, longitude, street, state, zip);
            if (id > 0) {
                mRowId = id;
            }
        } else {
            mDbHelper.updateNote(mRowId, title, body, latitude, longitude, street, state, zip);
        }
    }
    
}
