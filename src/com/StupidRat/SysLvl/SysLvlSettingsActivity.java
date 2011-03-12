package com.StupidRat.SysLvl;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SysLvlSettingsActivity extends SysLvlActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        
        
        
        final SharedPreferences prefs = getSharedPreferences(SYSLVL_PREFS , MODE_PRIVATE);
        
            
        Button resetButton = (Button) findViewById(R.id.ResetButton);
		resetButton.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				DatabaseHelper dbHelper = new DatabaseHelper(SysLvlSettingsActivity.this);
				dbHelper.onCreate(dbHelper.getWritableDatabase());
			}
		});
		
		final EditText editTextHighFreqPref = (EditText) findViewById(R.id.editTextHighFreqPref);
		//Load preference to an int.
		int setHighFreqInt = prefs.getInt("HighFreqOutput", 0);
		//Convert int into a string.
		String setHighFreq = String.valueOf(setHighFreqInt).trim();
		//Set text using the just converted string.
		editTextHighFreqPref.setText(setHighFreq);
		Log.v(SysLvlActivity.DEBUG_TAG, editTextHighFreqPref.getText().toString());
		
		editTextHighFreqPref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
		
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				
				//Assign editText input to a string to be converted to an Int.
				String str = editTextHighFreqPref.getText().toString().trim();
				//Convert String to an Int.
				Integer integer = Integer.valueOf(str); 
				//Open the editor and set the preference.
				Editor mEditor = prefs.edit();
				mEditor.putInt("HighFreqOutput", integer);
				//commit the changed preference
				mEditor.commit();
				
				TextView tvCurrentLvls = (TextView) findViewById(R.id.tvCurrentLvls);
				tvCurrentLvls.setText("Current starting levels: "+prefs.getInt("HighFreqOutput", 0)+"/"+prefs.getInt("LowFreqOutput", 0));
			}
		});
		
		final EditText editTextLowFreqPref = (EditText) findViewById(R.id.editTextLowFreqPref);
		//Load preference to an int.
		int setLowFreqInt = prefs.getInt("LowFreqOutput", 0);
		//Convert int into a string.
		String setLowFreq = String.valueOf(setLowFreqInt).trim();
		//Set text using the just converted string.
		
		editTextLowFreqPref.setText(setLowFreq);
		
		editTextLowFreqPref.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				
				
				//Assign editText input to a string to be converted to an Int.
				String str = editTextLowFreqPref.getText().toString().trim();
				//Convert String to an Int.
				Integer integer = Integer.valueOf(str); 
				//Open the editor and set the preference.
				Editor mEditor = prefs.edit();
				mEditor.putInt("LowFreqOutput", integer);
				//commit the changed preference
				mEditor.commit();
				
				TextView tvCurrentLvls = (TextView) findViewById(R.id.tvCurrentLvls);
				tvCurrentLvls.setText("Current starting levels: "+prefs.getInt("HighFreqOutput", 0)+"/"+prefs.getInt("LowFreqOutput", 0));
			}
		});
    }
   
    
    
}