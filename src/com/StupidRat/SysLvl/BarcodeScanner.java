package com.StupidRat.SysLvl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BarcodeScanner extends Activity {
	
	String contents = "";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.barcodetest);
        
    	Button btnStartScanner, btnSendEmail;
    	EditText etAccount, etBarcode;
    	
    	etBarcode = (EditText) findViewById(R.id.etBarcode);
    	etAccount = (EditText) findViewById(R.id.etAccount);
    	btnStartScanner = (Button) findViewById(R.id.scannerBtn);
    	btnSendEmail = (Button) findViewById(R.id.genEmail);
      
        btnStartScanner.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			    startActivityForResult(intent, 0);
			}
		});
        
        
        btnSendEmail.setOnClickListener(new View.OnClickListener(){
  			public void onClick(View view) {
  				EditText etBarcode = (EditText) findViewById(R.id.etBarcode);
  				EditText etAccount = (EditText) findViewById(R.id.etAccount);
  				final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
  				emailIntent.setType("plain/text");
  				emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"FargoDispatch@cableone.biz"});
  				emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "TECH REQUESTS MTA PROVISIONING");
  				emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Account: "+etAccount.getText().toString()+ "\nSerial: "+etBarcode.getText().toString());
  				
  				startActivity(Intent.createChooser(emailIntent, "Send mail..."));
  			}
  		});       
        
	}
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		    if (requestCode == 0) {
		      if (resultCode == RESULT_OK) {
		        contents = intent.getStringExtra("SCAN_RESULT");
		        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
		        EditText etBarcode = (EditText) findViewById(R.id.etBarcode);
		        etBarcode.setText(contents);
		       // showDialog("Suceeded", "Format: " + format + "\nContents: " + contents);
		      } else if (resultCode == RESULT_CANCELED) {
		        //showDialog("Failed", getString(R.string.result_failed_why));
		      }
		    }
	  }  
	  

}
