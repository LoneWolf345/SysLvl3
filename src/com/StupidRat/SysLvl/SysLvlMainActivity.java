package com.StupidRat.SysLvl;


import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

public class SysLvlMainActivity extends SysLvlActivity {
	
	private DatabaseAdapter dbHelper;
	private static final int ACTIVITY_CREATE = 0;
	//private static final int ACTIVITY_EDIT = 1;
	//private static final int DELETE_ID = Menu.FIRST + 1;
	
	
	
    /** Called when the activity is first created. **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        fillData();
                
    }
    
    public void onResume(){
    	super.onResume();
    	fillData();
    }

    public void createSpan(){
    	dbHelper.createSpan(100, "P3 .625", "26v2p", 0, 0, 0, 0);
    	
    	fillData();
    	//Intent i = new Intent(this, SpanDetails.class);
    	//startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
    	super.onActivityResult(requestCode, resultCode, intent);
    	
    	fillData();
    }
    
    public void fillData(){
    	
    	
    	
    	final SharedPreferences prefs = getSharedPreferences(SYSLVL_PREFS , MODE_PRIVATE);
    	TextView TextViewAmpOut = (TextView) findViewById(R.id.TextViewAmpOut);
    	
    	int AmpOutHigh = Integer.parseInt(prefs.getString("HighFreqOutput", "45"));
    	int AmpOutLow = Integer.parseInt(prefs.getString("LowFreqOutput", "36"));
    	//int AmpOutLow = prefs.getInt("LowFreqOutput", 0);
    	String AmpOutText = "Starting "+AmpOutHigh+"/"+AmpOutLow;
    	TextViewAmpOut.setText(AmpOutText);
    	
    	Log.v(SysLvlActivity.DEBUG_TAG, "Begin fillData");
    	ListView mList = (ListView) findViewById(R.id.l_list);
    	Button mButtonAddSpan = (Button) findViewById(R.id.ButtonAddSpan);
    	//Button mButtonCalculate	= (Button) findViewById(R.id.ButtonCalculate);
    	
    	Log.v(SysLvlActivity.DEBUG_TAG, "Set new QuickActionAdapter");
    	NewQAAdapter adapter = new NewQAAdapter(this);
        
    	Log.v(SysLvlActivity.DEBUG_TAG, "open database");
    	dbHelper = new DatabaseAdapter(this);
        dbHelper.open();
        dbHelper.updateAllSpanAttenuation();
        /**
         * Fetch all spans to populate the ListView
         */
        Log.v(SysLvlActivity.DEBUG_TAG, "Fetch all spans to populate the ListView");
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = dbHelper.fetchAllSpans();
        startManagingCursor(c);
        if (c != null){
        	if (c.moveToFirst()){
        		do {
        			int position = c.getInt(c.getColumnIndex("position"));
        			int distance = c.getInt(c.getColumnIndex("distance"));
        			String cableName = c.getString(c.getColumnIndex("cableName"));
        			String deviceName = c.getString(c.getColumnIndex("deviceName"));
        			double tapHigh = c.getDouble(c.getColumnIndex("tapHigh"));
        			double tapLow = c.getDouble(c.getColumnIndex("tapLow"));
        			double hotHigh = c.getDouble(c.getColumnIndex("hotHigh"));
        			double hotLow = c.getDouble(c.getColumnIndex("hotLow"));
        			result.add("" +distance+"ft of "+cableName+" to "+deviceName+"\n Tap: "+tapHigh+"/"+tapLow+" | Out: "+hotHigh+"/"+hotLow);
        			Log.i(null,position +" ,"+ cableName +" ,"+ deviceName);
        		}while (c.moveToNext());
        	}
        }
        //convert result to String array
        Log.v(SysLvlActivity.DEBUG_TAG, "convert result to String array");
        String[] data = new String[result.size()];
        result.toArray(data);
        
        Log.v(SysLvlActivity.DEBUG_TAG, "load data to adapter");
        adapter.setData(data);
        Log.v(SysLvlActivity.DEBUG_TAG, "load adapter to List");
        mList.setAdapter(adapter);
        
        
        /**
         * Create the items that appear on the popup
         **/
        Log.v(SysLvlActivity.DEBUG_TAG, "Create the items that appear on the pop-up");
        final ActionItem detailsAction = new ActionItem();
		
		detailsAction.setTitle("Details");
		detailsAction.setIcon(getResources().getDrawable(R.drawable.information));

		final ActionItem upAction = new ActionItem();
		
		upAction.setTitle("Up");
		upAction.setIcon(getResources().getDrawable(R.drawable.up));
		
		final ActionItem downAction = new ActionItem();
		
		downAction.setTitle("Down");
		downAction.setIcon(getResources().getDrawable(R.drawable.down));
		
		
		final ActionItem deleteAction = new ActionItem();
		
		deleteAction.setTitle("Delete");
		deleteAction.setIcon(getResources().getDrawable(R.drawable.delete));
		
		/**
		 * Set the onClickListeners for spans
		 */
		Log.v(SysLvlActivity.DEBUG_TAG, "Set the onClickListeners for the spans");
		mList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {		
				
				final long row = position;
				final long span_id = dbHelper.fetchSingleRowId(row);
				
				Log.v(SysLvlActivity.DEBUG_TAG, "Span clicked at position:"+row);
				//Toast.makeText(SysLvlMainActivity.this, "Span clicked at position:"+row, Toast.LENGTH_SHORT).show();
				
				final QuickAction mQuickAction 	= new QuickAction(view);
				final ImageView mMoreImage 		= (ImageView) view.findViewById(R.id.i_more);
				mMoreImage.setImageResource(R.drawable.ic_list_more_selected);
				
				detailsAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.v(SysLvlActivity.DEBUG_TAG, "Details button press for span "+row);
						
				    	try{
						Log.v(SysLvlActivity.DEBUG_TAG, "Setting up intent for SpanDetails on row "+row);
						Intent i = new Intent(SysLvlMainActivity.this, SpanDetails.class);
						
						Log.v(SysLvlActivity.DEBUG_TAG, "Set extras.");
						Log.v(SysLvlActivity.DEBUG_TAG, "mRowId: "+span_id);
						Log.v(SysLvlActivity.DEBUG_TAG, "mPosition: "+row);
						
						i.putExtra(DatabaseAdapter.KEY_ROWID, span_id);
						i.putExtra(DatabaseAdapter.KEY_POSITION, row);
						
						Log.v(SysLvlActivity.DEBUG_TAG, "Launching intent for SpanDetails on row "+row);
						startActivity(i);
				    	}catch (Exception e) {
				    		Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
						}						
						mQuickAction.dismiss();
					}
				});

				upAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.v(SysLvlActivity.DEBUG_TAG, "Up button press for span "+row);
						
						try{
							Log.v(SysLvlActivity.DEBUG_TAG, "Initiate MoveSpanUp");
							dbHelper.moveSpanUp(row);
							//dbHelper.updateAllSpanAttenuation();
							fillData();
						}catch(SQLException e){
							Log.e(SysLvlActivity.DEBUG_TAG, "up action failed");
							Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
						}
				    	
				    	
						
						//Toast.makeText(SysLvlMainActivity.this, "Move span from "+row+" to "+(row - 1), Toast.LENGTH_SHORT).show();
						mQuickAction.dismiss();
					}
				});
				
				downAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.v(SysLvlActivity.DEBUG_TAG, "Down button press for span "+row);
										    	
						try{
						dbHelper.moveSpanDown(row);
						//dbHelper.updateAllSpanAttenuation();
						fillData();
						}catch(SQLException e){
							Log.e(SysLvlActivity.DEBUG_TAG, "down action failed");
							Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
						}
						
						//Toast.makeText(SysLvlMainActivity.this, "Move span from "+(row)+" to "+(row + 1), Toast.LENGTH_SHORT).show();
						mQuickAction.dismiss();
					}
				});
							
				deleteAction.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Log.v(SysLvlActivity.DEBUG_TAG, "Delete button press for span "+row);
						//Toast.makeText(SysLvlMainActivity.this, "Delete span: "+row, Toast.LENGTH_SHORT).show();
				    try{	
				    	
						dbHelper.deleteSingleSpan(span_id);
						dbHelper.updateAllSpanPositions();
						fillData();
					}catch(SQLException e){
						Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
					}
						
						mQuickAction.dismiss();
					}
				});
				
				mQuickAction.addActionItem(detailsAction);
				mQuickAction.addActionItem(upAction);
				mQuickAction.addActionItem(downAction);
				mQuickAction.addActionItem(deleteAction);
				
				mQuickAction.setAnimStyle(QuickAction.ANIM_AUTO);
				
				mQuickAction.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss() {
						mMoreImage.setImageResource(R.drawable.ic_list_more);
					}
				});
				
				mQuickAction.show();
			}
		});
		
					
		mButtonAddSpan.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				//DatabaseHelper dbHelper = new DatabaseHelper(SysLvlMainActivity.this);
				//dbHelper.onCreate(dbHelper.getWritableDatabase());
				//fillData();
				createSpan();
			}
		});
		/**
		mButtonCalculate.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				//dbHelper.updateAllSpanAttenuation();
				fillData();
			}
		});
		**/
    }
}