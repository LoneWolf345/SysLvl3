package com.StupidRat.SysLvl;

import com.StupidRat.SysLvl.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class SysLvlMenuActivity extends SysLvlActivity {
    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        
        
        AdManager.setTestDevices(new String[]{
        		AdManager.TEST_EMULATOR, //Android emulator
        		"015DAA2D1200E011", // DROID X 701-212-6199
        });
        
        AdView adView = (AdView)findViewById(R.id.ad);
        adView.requestFreshAd();
        
        ListView menuList = (ListView) findViewById(R.id.ListView_Menu);
        String[] items = { getResources().getString(R.string.syslvl),
                getResources().getString(R.string.settings),
                getResources().getString(R.string.help),};
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(this, R.layout.menu_item, items);
        menuList.setAdapter(adapt);
        menuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            
        	public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                // Note: if the list was built "by hand" the id could be used.
                // As-is, though, each item has the same id
                TextView textView = (TextView) itemClicked;
                String strText = textView.getText().toString();
                if (strText.equalsIgnoreCase(getResources().getString(R.string.syslvl))) {
                    // Launch the SysLvl Activity
                    startActivity(new Intent(SysLvlMenuActivity.this, SysLvlMainActivity.class));
                } else if (strText.equalsIgnoreCase(getResources().getString(R.string.help))) {
                    // Launch the Help Activity
                    startActivity(new Intent(SysLvlMenuActivity.this, SysLvlHelpActivity.class));
                } else if (strText.equalsIgnoreCase(getResources().getString(R.string.settings))) {
                    // Launch the Settings Activity
                	//startActivity(new Intent(SysLvlMenuActivity.this, Preferences.class));
                	startActivityForResult(new Intent(SysLvlMenuActivity.this, Preferences.class), 0);
                	
                }
            }
        	
        });
        
        
    }
}