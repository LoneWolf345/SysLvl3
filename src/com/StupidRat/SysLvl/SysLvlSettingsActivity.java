package com.StupidRat.SysLvl;


import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
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
        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SysLvlDbHelper dbHelper = new SysLvlDbHelper(SysLvlSettingsActivity.this);
                dbHelper.onCreate(dbHelper.getWritableDatabase());
            }
        });

        final EditText editTextHighFreqPref = (EditText) findViewById(R.id.editTextHighFreqPref);
        int setHighFreqInt = prefs.getInt("HighFreqOutput", 0);
        String setHighFreq = String.valueOf(setHighFreqInt).trim();
        editTextHighFreqPref.setText(setHighFreq);

        editTextHighFreqPref.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTextHighFreqPref.setError(null);
                    return;
                }

                String str = editTextHighFreqPref.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    editTextHighFreqPref.setError("Please enter a number");
                    editTextHighFreqPref.setText(String.valueOf(prefs.getInt("HighFreqOutput", 0)));
                    return;
                }
                try {
                    Integer integer = Integer.valueOf(str);
                    Editor mEditor = prefs.edit();
                    mEditor.putInt("HighFreqOutput", integer);
                    mEditor.commit();

                    editTextHighFreqPref.setError(null);
                    updateCurrentLevels(prefs);
                } catch (NumberFormatException ex) {
                    editTextHighFreqPref.setError("Invalid number");
                    editTextHighFreqPref.setText(String.valueOf(prefs.getInt("HighFreqOutput", 0)));
                }
            }
        });

        final EditText editTextLowFreqPref = (EditText) findViewById(R.id.editTextLowFreqPref);
        int setLowFreqInt = prefs.getInt("LowFreqOutput", 0);
        String setLowFreq = String.valueOf(setLowFreqInt).trim();

        editTextLowFreqPref.setText(setLowFreq);

        editTextLowFreqPref.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editTextLowFreqPref.setError(null);
                    return;
                }

                String str = editTextLowFreqPref.getText().toString().trim();
                if (TextUtils.isEmpty(str)) {
                    editTextLowFreqPref.setError("Please enter a number");
                    editTextLowFreqPref.setText(String.valueOf(prefs.getInt("LowFreqOutput", 0)));
                    return;
                }
                try {
                    Integer integer = Integer.valueOf(str);
                    Editor mEditor = prefs.edit();
                    mEditor.putInt("LowFreqOutput", integer);
                    mEditor.commit();

                    editTextLowFreqPref.setError(null);
                    updateCurrentLevels(prefs);
                } catch (NumberFormatException ex) {
                    editTextLowFreqPref.setError("Invalid number");
                    editTextLowFreqPref.setText(String.valueOf(prefs.getInt("LowFreqOutput", 0)));
                }
            }
        });
    }

    private void updateCurrentLevels(SharedPreferences prefs) {
        TextView tvCurrentLvls = (TextView) findViewById(R.id.tvCurrentLvls);
        tvCurrentLvls.setText("Current starting levels: " + prefs.getInt("HighFreqOutput", 0) + "/" + prefs.getInt("LowFreqOutput", 0));
    }
}
