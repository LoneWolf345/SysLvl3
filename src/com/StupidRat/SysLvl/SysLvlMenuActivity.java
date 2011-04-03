package com.StupidRat.SysLvl;

import android.content.Intent;
import android.os.Bundle;
import com.admob.android.ads.AdManager;
import com.admob.android.ads.AdView;

public class SysLvlMenuActivity extends LauncherActivity {
    
	
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
				launcherButton.setEnabled(true);
				break;
			case 3:
				launcherButton.setText("Equipment");
				launcherButton.setIcon(R.drawable.barcode);
				launcherButton.setEnabled(false);
				break;
			case 4:
				launcherButton.setText("Find a Tech");
				launcherButton.setIcon(R.drawable.techs);
				launcherButton.setEnabled(false);
				break;
			case 5:
				launcherButton.setText("Settings");
				launcherButton.setIcon(R.drawable.settings);
				launcherButton.setEnabled(true);
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
				startActivity(new Intent(SysLvlMenuActivity.this, SysLvlMainActivity.class));
				break;
			case 2: //GeoNotes
				startActivityForResult(new Intent(SysLvlMenuActivity.this, GeoNotes.class), 0);
				break;
			case 3: //Equipment
				
				break;
			case 4: //Find a Tech
				
				break;
			case 5: //Settings
				startActivityForResult(new Intent(SysLvlMenuActivity.this, Preferences.class), 0);
				break;
			case 6: //Exit
				this.finish();
			}
		}
		
	}

	@Override
	public boolean onLongClick(LauncherActivity.LauncherButton launcherButton) {
		//launcherButton.setText("Long Click!");
		return true;
	}

}