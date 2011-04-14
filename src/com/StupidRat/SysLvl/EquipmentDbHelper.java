package com.StupidRat.SysLvl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EquipmentDbHelper extends SQLiteOpenHelper{
	
	private static final int DB_VERSION = 10;
	private static final String DB_NAME = "EquipmentDB";

	/**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     */
	public EquipmentDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public void createEquipmentTable(SQLiteDatabase db){
		try{
			db.execSQL("DROP TABLE IF EXISTS 'Equipment'");
			db.execSQL("CREATE TABLE 'Equipment' ('_id' INTEGER PRIMARY KEY  NOT NULL ,'Serial' TEXT, 'Mac' TEXT,'Model' TEXT,'Type' TEXT,'Status' TEXT)");

		}catch(SQLException e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
		
	}

}
