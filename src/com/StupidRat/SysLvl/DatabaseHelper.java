package com.StupidRat.SysLvl;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int DB_VERSION = 10;
	private static final String DB_NAME = "SysLvlDB";

	
	/**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     */
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		fillCableDB(db);
		fillTapsDB(db);
		
		fillSampleSpans(db);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destrou all old data");
		
		db.execSQL("DROP TABLE IF EXISTS Spans");
		db.execSQL("DROP TABLE IF EXISTS CableDB");
		db.execSQL("DROP TABLE IF EXISTS TapsDB");
		onCreate(db);
		Log.v(SysLvlActivity.DEBUG_TAG, "Upgrade Complete");
	}

	public void fillCableDB(SQLiteDatabase db){
		Log.v(SysLvlActivity.DEBUG_TAG, "start fillCableDB");
		try{
			db.execSQL("DROP TABLE IF EXISTS 'CableDB'");
			db.execSQL("CREATE TABLE 'CableDB' ('_id' INTEGER PRIMARY KEY NOT NULL, 'name' TEXT NOT NULL ,'manufacturer' TEXT NOT NULL ,'series' TEXT NOT NULL ,'visible' BOOL NOT NULL ,'mhz5' DOUBLE NOT NULL ,'mhz55' DOUBLE NOT NULL ,'mhz550' DOUBLE NOT NULL )");
			db.execSQL("INSERT INTO 'CableDB' VALUES(0,'RG-59','Comscope','Drop','True',0.86,2.05,5.95)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(1,'RG-6','Comscope','Drop','True',0.58,1.6,4.66)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(2,'RG-7','Comscope','Drop','False',0.47,1.25,3.85)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(3,'RG-11','Comscope','Drop','True',0.38,0.96,3.04)");
			
			db.execSQL("INSERT INTO 'CableDB' VALUES(4,'P3 .412','Comscope','PIII','False',0.2,0.69,2.35)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(5,'P3 .500','Comscope','PIII','True',0.16,0.54,1.82)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(6,'P3 .625','Comscope','PIII','True',0.13,0.46,1.5)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(7,'P3 .700','Comscope','PIII','False', 0.11, 0.36, 1.25)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(8,'P3 .750','Comscope','PIII','True', 0.11, 0.37, 1.24)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(9,'P3 .840','Comscope','PIII','False', 0.09, 0.32, 1.09)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(10,'P3 .875','Comscope','PIII','True', 0.09, 0.33, 1.08)");
			
			db.execSQL("INSERT INTO 'CableDB' VALUES(11,'QR .300','Comscope','QR','False', 0.24, 0.84, 2.85)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(12,'QR .540','Comscope','QR','False', 0.14, 0.47, 1.56)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(13,'QR .715','Comscope','QR','False', 0.11, 0.36, 1.25)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(14,'QR .860','Comscope','QR','False', 0.09, 0.32, 1.06)");
			
			db.execSQL("INSERT INTO 'CableDB' VALUES(15,'MC2 .500','Comscope','MC2','False', 0.15, 0.49, 1.6)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(16,'MC2 .650','Comscope','MC2','False', 0.12, 0.39, 1.29)");
			db.execSQL("INSERT INTO 'CableDB' VALUES(17,'MC2 .750','Comscope','MC2','False', 0.11, 0.36, 1.06)");
			//db.execSQL("INSERT INTO 'CableDB' VALUES(null,'name','manufacturer','series','visible', 5mhz, 55mhz, 550mhz)");
			
			Log.v(SysLvlActivity.DEBUG_TAG, "fillCableDB completed successfully");
		}catch(SQLException e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
		
	}
	
	public void fillSampleSpans(SQLiteDatabase db){
		Log.v(SysLvlActivity.DEBUG_TAG, "start fillSampleSpans");
		try{
			db.execSQL("DROP TABLE IF EXISTS 'Spans'");
			db.execSQL("CREATE TABLE 'Spans' ('_id' INTEGER PRIMARY KEY  NOT NULL ,'position' INTEGER, 'distance' INTEGER,'cableName' TEXT,'deviceName' TEXT,'tapHigh' DOUBLE,'tapLow' DOUBLE,'hotHigh' DOUBLE,'hotLow' DOUBLE)");
			db.execSQL("INSERT INTO 'Spans' VALUES(0,0,100,'P3 .625','26v2p',0,0,0,0)");
			db.execSQL("INSERT INTO 'Spans' VALUES(1,1,100,'P3 .625','26v2p',0,0,0,0)");
			db.execSQL("INSERT INTO 'Spans' VALUES(2,2,100,'P3 .625','20v2p',0,0,0,0)");
			db.execSQL("INSERT INTO 'Spans' VALUES(3,3,100,'P3 .625','17v2p',0,0,0,0)");
			db.execSQL("INSERT INTO 'Spans' VALUES(4,4,100,'P3 .625','14v2p',0,0,0,0)");
			
			Log.v(SysLvlActivity.DEBUG_TAG, "fillSampleSpans completed successfully");
		}catch(SQLException e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
		
	}
   
	public void fillTapsDB(SQLiteDatabase db){
		Log.v(SysLvlActivity.DEBUG_TAG, "start fillTapsDB");
		try{
			db.execSQL("DROP TABLE IF EXISTS 'TapsDB'");
			db.execSQL("CREATE TABLE 'TapsDB' ('_id' INTEGER PRIMARY KEY  NOT NULL ,'name' TEXT,'manufacturer' TEXT,'ports' INTEGER,'value' INTEGER,'terminating' BOOL,'color' TEXT,'tapmhz5' DOUBLE,'tapmhz55' DOUBLE,'tapmhz550' DOUBLE,'hotmhz5' DOUBLE,'hotmhz55' DOUBLE,'hotmhz550' DOUBLE)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(0,'4v2p','Scientific Atlanta',2,4,'True','Pink',4,4,4,NULL,NULL,NULL)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(1,'8v2p','Scientific Atlanta',2,8,'True','Gray',8,8,8,3.2,3,3.7)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(2,'11v2p','Scientific Atlanta',2,11,'True','Brown',11,11,1,1.7,1.7,2.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(3,'14v2p','Scientific Atlanta',2,14,'True','Yellow',14,14,14,0.9,1.1,1.6)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(4,'17v2p','Scientific Atlanta',2,17,'True','Yellow',17,17,17,0.5,0.6,1.1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(5,'20v2p','Scientific Atlanta',2,20,'True','Black',20,20,20,0.4,0.7,1.1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(6,'23v2p','Scientific Atlanta',2,23,'True','Orange',23,23,23,0.4,0.6,1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(7,'26v2p','Scientific Atlanta',2,26,'True','Blue',26,26,26,0.3,0.3,0.8)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(8,'29v2p','Scientific Atlanta',2,29,'False','White',29,29,29,0.3,0.5,0.8)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(9,'32v2p','Scientific Atlanta',2,32,'False','Red',32,32,32,0.3,0.5,0.8)");
			
			db.execSQL("INSERT INTO 'TapsDB' VALUES(10,'8v4p','Scientific Atlanta',4,8,'True','Grey',8,8,8,NULL,NULL,NULL)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(11,'11v4p','Scientific Atlanta',4,11,'True','Brown',11,11,11,3.2,3,3.7)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(12,'14v4p','Scientific Atlanta',4,14,'True','Yellow',14,14,14,1.6,1.7,2.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(13,'17v4p','Scientific Atlanta',4,17,'True','Purple',17,17,17,1,1,1.5)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(14,'20v4p','Scientific Atlanta',4,20,'True','Black',20,20,20,0.6,0.7,1.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(15,'23v4p','Scientific Atlanta',4,23,'True','Orange',23,23,23,0.5,0.7,1.1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(16,'26v4p','Scientific Atlanta',4,26,'True','Blue',26,26,26,0.4,0.5,0.9)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(17,'29v4p','Scientific Atlanta',4,29,'False','White',29,29,29,0.3,0.5,0.8)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(18,'32v4p','Scientific Atlanta',4,32,'False','Red',32,32,32,0.3,0.3,0.8)");
			
			db.execSQL("INSERT INTO 'TapsDB' VALUES(19,'11v8p','Scientific Atlanta',8,11,'True','Brown',11,11,11,NULL,NULL,NULL)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(20,'14v8p','Scientific Atlanta',8,14,'True','Yellow',14,14,14,3.2,3,3.7)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(21,'17v8p','Scientific Atlanta',8,17,'True','Purple',17,17,17,1.6,1.7,2.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(22,'20v8p','Scientific Atlanta',8,20,'True','Black',20,20,20,1,1,1.5)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(23,'23v8p','Scientific Atlanta',8,23,'True','Orange',23,23,23,0.6,0.7,1.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(24,'26v8p','Scientific Atlanta',8,26,'True','Blue',26,26,26,0.5,0.7,1.1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(25,'29v8p','Scientific Atlanta',8,29,'True','White',29,29,29,0.4,0.5,0.9)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(26,'32v8p','Scientific Atlanta',8,32,'True','Red',32,32,32,0.3,0.5,0.8)");
			
			db.execSQL("INSERT INTO 'TapsDB' VALUES(27, 'DC8', 'Scientific Atlanta', 0, 8, 'True', 'Default', 8.5, 8.3, 8.3, 1.7, 1.4, 1.7)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(28, 'DC12', 'Scientific Atlanta', 0, 12, 'True', 'Default', 13, 12.5, 12.3, 0.9, 0.8, 1)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(29, 'DC16', 'Scientific Atlanta', 0, 16, 'True', 'Default', 16.2, 15.8, 15.8, 0.9, 0.7, 0.9)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(30, '2way', 'Scientific Atlanta', 0, 0, 'True', 'Default', 3.9, 3.6, 3.8, 3.9, 3.6, 3.8)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(31, 'Bal3way', 'Scientific Atlanta', 0, 0, 'True', 'Default', 5.5, 5.3, 5.6, 5.5, 5.3, 5.6)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(32, 'UB3wayCold', 'Scientific Atlanta', 0, 0, 'True', 'Default', 3.7, 3.5, 3.7, 7.3, 6.8, 7.2)");
			db.execSQL("INSERT INTO 'TapsDB' VALUES(33, 'UB3wayHot', 'Scientific Atlanta', 0, 0, 'True', 'Default', 7.3, 6.8, 7.2, 3.7, 3.5, 3.7)");
			//db.execSQL("INSERT INTO 'TapsDB' VALUES(#, 'name', 'manufacturer', ports, value, 'visible', 'color', 0, 0, 0, 0, 0, 0)");
			
			Log.v(SysLvlActivity.DEBUG_TAG, "fillTapsDB completed successfully");
		}catch(SQLException e){
			Log.e(SysLvlActivity.DEBUG_TAG, e.toString());
		}
	}
}
