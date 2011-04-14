package com.StupidRat.SysLvl;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class MenuScreen extends LauncherActivity {
    
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);       

        
        AdManager.setTestDevices(new String[]{
        		AdManager.TEST_EMULATOR, //Android emulator
        		"015DAA2D1200E011", // DROID X 701-212-6199
        });
        
//        AdView adView = (AdView)findViewById(R.id.ad);
//        adView.requestFreshAd();
 
        
    }
    
    
	@Override
	public void onCreateLauncherButton(LauncherActivity.LauncherButton launcherButton) {
		// Add events etc.
		//if (fIcons[launcherButton.getPosition()] != 0) {
		if (launcherButton.getPosition() != 0) {
			int i = launcherButton.getPosition();
			switch(i){
			case 1:
				launcherButton.setText("SysLvl");
				launcherButton.setIcon(R.drawable.syslvl);
				launcherButton.setEnabled(true);
				break;
			case 2:
				launcherButton.setText("GeoNotes");
				launcherButton.setIcon(R.drawable.globe);
				launcherButton.setEnabled(false);
				break;
			case 3:
				launcherButton.setText("Equipment");
				launcherButton.setIcon(R.drawable.barcode);
				launcherButton.setEnabled(true);
				break;
			case 4:
				launcherButton.setText("Find a Tech");
				launcherButton.setIcon(R.drawable.techs);
				launcherButton.setEnabled(false);
				break;
			case 5:
				launcherButton.setText("Job Log");
				launcherButton.setIcon(R.drawable.folders);
				launcherButton.setEnabled(false);
				break;
			case 6:
				launcherButton.setText("Exit");
				launcherButton.setIcon(R.drawable.exit);
				launcherButton.setEnabled(true);
			}
		}
		launcherButton.setButtonListener(this);
	}

	@Override
	public void onClick(LauncherActivity.LauncherButton launcherButton) {
		if (launcherButton.getPosition() != 0) {
			int i = launcherButton.getPosition();
			switch(i){
			case 1:	//Syslvl
				startActivity(new Intent(MenuScreen.this, SysLvlScreen.class));
				break;
			case 2: //GeoNotes
				startActivityForResult(new Intent(MenuScreen.this, GeoNotesScreen.class), 0);
				break;
			case 3: //Equipment
				startActivityForResult(new Intent(MenuScreen.this, EquipmentScreen.class), 0);
				break;
			case 4: //Find a Tech
				
				break;
			case 5: //Job Log
				
				break;
			case 6: //Exit
				this.finish();
			}
		}
		
	}

	
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		menu.add(0, menu.FIRST, menu.NONE, "Settings");
		startActivityForResult(new Intent(MenuScreen.this, Preferences.class), 0);
		return true;
		
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()){
		
		case (Menu.FIRST):
			
			return true;
		
		}
		
		return false;
	}

}