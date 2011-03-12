package com.StupidRat.SysLvl;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.StupidRat.SysLvl.DatabaseAdapter;

public class SpanDetails extends Activity {

	private DatabaseAdapter mDbHelper;
	private Long mRowId;
	private Long mPosition;
	
	
	@Override
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		
		
		mDbHelper = new DatabaseAdapter(this);
		mDbHelper.open();
		
		setContentView(R.layout.span_details);
	
		Button mButtonUpdate = (Button) findViewById(R.id.ButtonUpdate);
		Button mButtonDelete = (Button) findViewById(R.id.ButtonDelete);
		
		Log.v(SysLvlActivity.DEBUG_TAG, "Pull extras mRowId, mPosition");
		try{
		mRowId = null;
		mPosition = null;
		Bundle extras = getIntent().getExtras();
		mRowId = (bundle == null) ? null : (Long) bundle.getSerializable(DatabaseAdapter.KEY_ROWID);
		mPosition = (bundle == null) ? null : (Long) bundle.getSerializable(DatabaseAdapter.KEY_POSITION);

		if (extras != null){
			mRowId = extras.getLong(DatabaseAdapter.KEY_ROWID);
			mPosition = extras.getLong(DatabaseAdapter.KEY_POSITION);
		}
		
		Log.v(SysLvlActivity.DEBUG_TAG, "mRowId: "+mRowId);
		Log.v(SysLvlActivity.DEBUG_TAG, "mPosition: "+mPosition);
		
		populateFields();
		}catch(Exception e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
		
		
		mButtonUpdate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				setResult(RESULT_OK);
				finish();
			}
		});
		
		mButtonDelete.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view){
				mDbHelper.deleteSingleSpan(mRowId);
				mDbHelper.updateAllSpanPositions();
				finish();
			}
		});
		
	}
	
	private void populateFields(){
		
		try{
		Log.v(SysLvlActivity.DEBUG_TAG, "Starting to populate Fields");
		
		EditText mEditLength = (EditText) findViewById(R.id.EditTextLength);
		
		TextView mTapHighDetail = (TextView) findViewById(R.id.TextViewTapHighDetail);
		TextView mTapLowDetail = (TextView) findViewById(R.id.TextViewTapLowDetail);
		
		TextView mHotHighDetail = (TextView) findViewById(R.id.TextViewHotHighDetail);
		TextView mHotLowDetail = (TextView) findViewById(R.id.TextViewHotLowDetail);
		
		Spinner mSpinnerDevice = (Spinner) findViewById(R.id.SpinnerDevice);
		Spinner	mSpinnerCableType = (Spinner) findViewById(R.id.SpinnerCableType);

		/**
		//-----Begin Test-----------------------------
		Log.v(SysLvlActivity.DEBUG_TAG, "-------------- Begin Test Section -----------");
		try{
			ArrayList<String> results = new ArrayList<String>();
			Cursor c = mDbHelper.fetchAllCables();
			if (c!= null){
				if (c.moveToFirst()){
					Log.v(SysLvlActivity.DEBUG_TAG, "Begin fill ArrayList Loop");
					do {
						String name = c.getString(1);
						Log.v(SysLvlActivity.DEBUG_TAG, "name: "+name);
						results.add(name);
					}while (c.moveToNext());
				}
			}
			
			//Create an array to specify the fields we want to display in the list (only the 'name' column in this case)
			String[] from = new String[]{DatabaseAdapter.KEY_NAME}; 
			// and an array of the fields we want to bind those fields to (in this case just the textView 'tvDBViewRow' from our new db_view_row.xml layout above) 
			int[] to = new int[]{R.id.tvDBViewRow};
			SimpleCursorAdapter cableSpinnerAdapter = new SimpleCursorAdapter(this, R.layout.spinner_row_view, c, from, to);
			
			Log.v(SysLvlActivity.DEBUG_TAG, "Cable spinner");
		    //ArrayAdapter<CharSequence> cableSpinnerAdapter = ArrayAdapter.createFromResource(
		    //		this, R.array.CableList, android.R.layout.simple_spinner_item);
		    cableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		    mSpinnerCableType.setAdapter(cableSpinnerAdapter);
			
		}catch(IllegalStateException e){
			Log.v(SysLvlActivity.DEBUG_TAG, e.toString());
		}
		**/
	    
		
		//Populate cable spinner
		Log.v(SysLvlActivity.DEBUG_TAG, "Cable spinner");
	    ArrayAdapter<CharSequence> cableSpinnerAdapter = ArrayAdapter.createFromResource(
	    		this, R.array.CableList, android.R.layout.simple_spinner_item);
	    cableSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSpinnerCableType.setAdapter(cableSpinnerAdapter);
	   
	  //--------End Test------------------
		Log.v(SysLvlActivity.DEBUG_TAG, "-------------- End Test Section -----------");
		
		
	    //Set cableName spinner
		Log.v(SysLvlActivity.DEBUG_TAG, "Try and set the cable spinner");
		try{
			Cursor cable = mDbHelper.fetchSingleSpan(mRowId);
			String cableName = cable.getString(cable.getColumnIndexOrThrow(DatabaseAdapter.KEY_CABLENAME));
			Log.v(SysLvlActivity.DEBUG_TAG, "cableName: "+cableName);
			
			for (int i=0; i<mSpinnerCableType.getCount(); i++){
				String s = (String) mSpinnerCableType.getItemAtPosition(i);
				Log.v(SysLvlActivity.DEBUG_TAG, s + " " + cableName);
			if (s.equalsIgnoreCase(cableName)){
				mSpinnerCableType.setSelection(i);
				}
			}
			Log.v(SysLvlActivity.DEBUG_TAG, "cable spinner sucessfully set up");
		}catch(SQLException e){
			Log.v(SysLvlActivity.DEBUG_TAG, "Failed to set cable spinner");
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}

	    
	    //Populate device spinner
	    Log.v(SysLvlActivity.DEBUG_TAG, "Device spinner");
	    ArrayAdapter<CharSequence> deviceSpinnerAdapter = ArrayAdapter.createFromResource(
	    		this, R.array.DeviceList, android.R.layout.simple_spinner_item);
	    deviceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    mSpinnerDevice.setAdapter(deviceSpinnerAdapter);
	    Log.v(SysLvlActivity.DEBUG_TAG, "Check if rowId is not null. rowId is: " +mRowId.toString());
		
	    //Set deviceName spinner
		Log.v(SysLvlActivity.DEBUG_TAG, "Try and set the device spinner");
		try{
			Cursor device = mDbHelper.fetchSingleSpan(mRowId);
			String deviceName = device.getString(device.getColumnIndexOrThrow(DatabaseAdapter.KEY_DEVICENAME));
			for (int i=0; i<mSpinnerDevice.getCount(); i++){
				String s = (String) mSpinnerDevice.getItemAtPosition(i);
				Log.e(null, s + " " + deviceName);
				if (s.equalsIgnoreCase(deviceName)){
					mSpinnerDevice.setSelection(i);
				}
			}
			Log.v(SysLvlActivity.DEBUG_TAG, "device spinner sucessfully set up");
		}catch(SQLException e){
			Log.v(SysLvlActivity.DEBUG_TAG, "Failed to set cable spinner");
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
	    
	    if (mRowId != null){
	    	try{
	    		Log.v(SysLvlActivity.DEBUG_TAG, "Fetchspan at "+mRowId+" and load to cursor");
	    		Cursor span = mDbHelper.fetchSingleSpan(mRowId);
				startManagingCursor(span);
				

				//Set cableLength EditText
				mEditLength.setText(span.getString(span.getColumnIndexOrThrow(DatabaseAdapter.KEY_DISTANCE)));
				
				
				//Set signal level TextViews
				mTapHighDetail.setText(span.getString(span.getColumnIndexOrThrow(DatabaseAdapter.KEY_TAPHIGH)));
				mTapLowDetail.setText(span.getString(span.getColumnIndexOrThrow(DatabaseAdapter.KEY_TAPLOW)));
				mHotHighDetail.setText(span.getString(span.getColumnIndexOrThrow(DatabaseAdapter.KEY_HOTHIGH)));
				mHotLowDetail.setText(span.getString(span.getColumnIndexOrThrow(DatabaseAdapter.KEY_HOTLOW)));
				
				
	    	}catch(SQLException e){
	    		Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
	    	}
	    	

			}
		}catch(SQLException e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}

	}
	
	protected void onSaveInstanceState(Bundle outState){
		super.onSaveInstanceState(outState);
		saveState();
		outState.putSerializable(DatabaseAdapter.KEY_ROWID, mRowId);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		saveState();
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		populateFields();
	}
	
	private void saveState(){
		
		
		try{
		EditText mEditLength = (EditText) findViewById(R.id.EditTextLength);
		
		TextView mTapHighDetail = (TextView) findViewById(R.id.TextViewTapHighDetail);
		TextView mTapLowDetail = (TextView) findViewById(R.id.TextViewTapLowDetail);
		
		TextView mHotHighDetail = (TextView) findViewById(R.id.TextViewHotHighDetail);
		TextView mHotLowDetail = (TextView) findViewById(R.id.TextViewHotLowDetail);
		
		Spinner mSpinnerDevice = (Spinner) findViewById(R.id.SpinnerDevice);
		Spinner	mSpinnerCableType = (Spinner) findViewById(R.id.SpinnerCableType);
		
		String cableName = (String) mSpinnerCableType.getSelectedItem();
		String deviceName = (String) mSpinnerDevice.getSelectedItem();
		
		String strdistance = (String) mEditLength.getText().toString();
		int distance = Integer.valueOf((String)strdistance);
		
		String strTapHigh = (String) mTapHighDetail.getText().toString();
		double tapHigh = Double.valueOf((String)strTapHigh);
		
		String strTapLow = (String) mTapLowDetail.getText().toString();
		double tapLow = Double.valueOf((String)strTapLow);
		
		String strHotHigh = (String) mHotHighDetail.getText().toString();
		double hotHigh = Double.valueOf((String)strHotHigh);
		
		String strHotLow = (String) mHotLowDetail.getText().toString();
		double hotLow = Double.valueOf((String)strHotLow);
 

		if (mRowId != null){
			Log.v(SysLvlActivity.DEBUG_TAG, "UPDATE SPAN mRowId: "+mRowId+", position: "+mPosition+", distance: "+distance+", cableName: "+cableName+", deviceName: "+deviceName);
			mDbHelper.updateSingleSpan(mRowId, mPosition , distance, cableName, deviceName, tapHigh, tapLow, hotHigh, hotLow);
			
		}

		}catch(Exception e){
			Log.e(SysLvlActivity.DEBUG_TAG, "Failed to SaveState");
			Log.e(SysLvlActivity.DEBUG_TAG, "Error: "+e.toString());
		}
		
	}
	
}
