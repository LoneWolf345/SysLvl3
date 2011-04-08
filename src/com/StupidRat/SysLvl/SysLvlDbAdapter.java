package com.StupidRat.SysLvl;

import java.math.BigDecimal;

import com.StupidRat.SysLvl.SysLvlSettingsActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SysLvlDbAdapter {

	private static final String SPANS_TABLE = "Spans";
	private static final String CABLES_TABLE = "CableDB";
	private static final String TAPS_TABLE = "TapsDB";

	private Context context;
	private SQLiteDatabase database;
	private SysLvlDbHelper dbHelper;

	public static final String KEY_ROWID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_VISIBLE = "visible";
	public static final String KEY_POSITION = "position";
	public static final String KEY_DISTANCE = "distance";
	public static final String KEY_CABLEID = "cableId";
	public static final String KEY_CABLENAME = "cableName";
	public static final String KEY_DEVICEID = "deviceId";
	public static final String KEY_DEVICENAME = "deviceName";
	public static final String KEY_TAPHIGH = "tapHigh";
	public static final String KEY_TAPLOW = "tapLow";
	public static final String KEY_HOTHIGH = "hotHigh";
	public static final String KEY_HOTLOW = "hotLow";
	public static final String KEY_LOWFREQUENCY = "mhz50";
	public static final String KEY_HIGHFREQUENCY = "mhz550";
	//public static final String SYSLVL_PREFS = "SysLvlPrefs";
	

	public SysLvlDbAdapter(Context context) {
		this.context = context;

	}

	public SysLvlDbAdapter open() throws SQLException {
		dbHelper = new SysLvlDbHelper(context);
		database = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create Functions
	 */
	public void createSpan(int distance, String cableName, String deviceName, double tapHigh, double tapLow, double hotHigh, double hotLow) {
		
		long nextPosition = fetchSpanCount();
		String sql = "INSERT INTO '"+SPANS_TABLE+"' VALUES (null,'"+nextPosition+"','"+distance+"','"+cableName+"','"+deviceName+"','0','0','0','0')";
		Log.v(SysLvlActivity.DEBUG_TAG, sql);
		database.execSQL(sql);
		
	}

	/**
	 * Fetch Functions
	 */
	public long fetchSpanCount() {
		Cursor mCursor = fetchAllSpans();
		long result = mCursor.getCount();

		return result;
	}
	
	public Cursor fetchAllSpans() {
		Log.v(SysLvlActivity.DEBUG_TAG, "fetchAllSpans");

		String sql = "SELECT * FROM " + SPANS_TABLE + " WHERE " + KEY_POSITION
				+ " >= 0 GROUP BY position";
		Log.v(SysLvlActivity.DEBUG_TAG, sql);

		return database.rawQuery(sql, null);
	}
	
	public Cursor fetchSingleSpan(long rowId) throws SQLException {

		String sql = "SELECT * FROM " + SPANS_TABLE + " WHERE " + KEY_ROWID
				+ " = " + rowId;

		Log.v(SysLvlActivity.DEBUG_TAG, "Fetch span with rowId: " + rowId);
		Log.v(SysLvlActivity.DEBUG_TAG, sql);
		Cursor mCursor = database.rawQuery(sql, null);
		Log.v(SysLvlActivity.DEBUG_TAG, "Cursor returned " + mCursor.getCount()
				+ " rows.");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetchAllCables() throws SQLException {
		String sql = "SELECT " + KEY_ROWID +", "+KEY_NAME+ " FROM " + CABLES_TABLE + " WHERE "
				+ KEY_VISIBLE + " = 'True' GROUP BY " + KEY_ROWID;

		Log.v(SysLvlActivity.DEBUG_TAG, "start fetchAllCables");
		Log.v(SysLvlActivity.DEBUG_TAG, sql);
		Cursor mCursor = database.rawQuery(sql, null);
		Log.v(SysLvlActivity.DEBUG_TAG, "Cursor returned " + mCursor.getCount()
				+ " rows.");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		Log.v(SysLvlActivity.DEBUG_TAG, "fetchCables complete");
		return mCursor;
	}
	
	public Cursor fetchSingleCable(String name) throws SQLException {

		String sql = "SELECT * FROM " + CABLES_TABLE + " WHERE " + KEY_NAME
				+ " = '" + name+"'";

		Log.v(SysLvlActivity.DEBUG_TAG, "Fetch cable with name: " + name);
		Log.v(SysLvlActivity.DEBUG_TAG, sql);
		Cursor mCursor = database.rawQuery(sql, null);
		Log.v(SysLvlActivity.DEBUG_TAG, "Cursor returned " + mCursor.getCount()
				+ " rows.");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetchAllDevices() throws SQLException {
		Log.v(SysLvlActivity.DEBUG_TAG, "start fetchTaps");

		Cursor mCursor = database.rawQuery("SELECT name FROM TapsDB", null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		Log.v(SysLvlActivity.DEBUG_TAG, "fetchTaps completed");
		return mCursor;
	}
	
	public Cursor fetchSingleDevice(String device) throws SQLException {

		String sql = "SELECT * FROM " + TAPS_TABLE + " WHERE " + KEY_NAME
				+ " = '" + device+"'";

		Log.v(SysLvlActivity.DEBUG_TAG, "Fetch device with name: '" + device+"'");
		Log.v(SysLvlActivity.DEBUG_TAG, sql);
		Cursor mCursor = database.rawQuery(sql, null);
		Log.v(SysLvlActivity.DEBUG_TAG, "Cursor returned " + mCursor.getCount()
				+ " rows.");

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public long fetchSingleRowId(long position) throws SQLException {
		Log.v(SysLvlActivity.DEBUG_TAG, "start fetchRowId");
		Log.v(SysLvlActivity.DEBUG_TAG, "fetchRowId for position: " + position);

		String sql = "SELECT " + KEY_ROWID + " FROM " + SPANS_TABLE + " WHERE "
				+ KEY_POSITION + " = " + position;
		Log.v(SysLvlActivity.DEBUG_TAG, sql);

		Cursor mCursor = database.rawQuery(sql, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		long rowId = mCursor.getLong(mCursor
				.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_ROWID));
		Log.v(SysLvlActivity.DEBUG_TAG,
				"fetchRowId completed successfully returning rowId: " + rowId);
		return rowId;

	}
	
	/**
	 * Update Functions
	 */
	public boolean updateSingleSpan(long rowId, long position, int distance,
			String cableName, String deviceName, double tapHigh, double tapLow,
			double hotHigh, double hotLow) {

		try {
			String sql = "UPDATE " + SPANS_TABLE + " SET " + KEY_POSITION
					+ " = " + position + ", " + KEY_DISTANCE + " = " + distance
					+ ", " + KEY_CABLENAME + " = '" + cableName + "', "
					+ KEY_DEVICENAME + " = '" + deviceName + "', "
					+ KEY_TAPHIGH + " = " + tapHigh + ", " + KEY_TAPLOW + " = "
					+ tapLow + ", " + KEY_HOTHIGH + " = " + hotHigh + ", "
					+ KEY_HOTLOW + " = " + hotLow + "  WHERE " + KEY_ROWID
					+ " = " + rowId;

			Log.v(SysLvlActivity.DEBUG_TAG, "Begin updateSpan");
			Log.v(SysLvlActivity.DEBUG_TAG, sql);
			database.execSQL(sql);
			Log.v(SysLvlActivity.DEBUG_TAG,
					"updateSpan completed without errors.");

			return true;
		} catch (SQLException e) {
			Log.e(SysLvlActivity.DEBUG_TAG, "Failed to update span.");
			Log.e(SysLvlActivity.DEBUG_TAG, "Error: " + e.toString());
			return false;
		}

	}

	public void updateAllSpanPositions() {
		Cursor c = fetchAllSpans();

		if (c != null) {
			c.moveToFirst();
			Log.v(SysLvlActivity.DEBUG_TAG, "Begin refreshSpanPositions loop.");
			for (int i = 0; i < fetchSpanCount(); i++) {
				long rowId = c.getLong(c
						.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_ROWID));
				String sql = "UPDATE " + SPANS_TABLE + " SET " + KEY_POSITION
						+ " = " + i + " WHERE " + KEY_ROWID + " = " + rowId;
				Log.v(SysLvlActivity.DEBUG_TAG, sql);
				database.execSQL(sql);
				c.moveToNext();
			}
		}

	}
	
	public void updateAllSpanAttenuation() {
		
		//Get a cursor with all spans
		Cursor s = fetchAllSpans();
		
		//Create necessary variables
		String cableName = null;
		double cableLossRateLow = 0;
		double cableLossRateHigh = 0;
		
		String deviceName = null;
		double tapLossLow = 0;
		double tapLossHigh = 0;
		double hotLossLow = 0;
		double hotLossHigh = 0;
		
		double length = 0;
		
		double incomingSignalHigh = 0;
		double incomingSignalLow = 0;
		
		//Get starting signal levels from user preferences
		SharedPreferences prefs = context.getSharedPreferences(SysLvlActivity.SYSLVL_PREFS, 0);
		incomingSignalHigh = Integer.parseInt(prefs.getString("HighFreqOutput", "45"));
		incomingSignalLow = Integer.parseInt(prefs.getString("LowFreqOutput", "36"));
		
		//Begin a loop that loads levels from a cursor, performs the calculations
		//and saves the new levels.
		if (s != null) {
						
			Log.v(SysLvlActivity.DEBUG_TAG,"Begin updateAllSpanAttenuation loop.");

			s.moveToFirst();
			for (int i = 0; i < fetchSpanCount(); i++) {
				
				Log.v(SysLvlActivity.DEBUG_TAG,"\n------------ Span "+i+" ------------");
				Log.v(SysLvlActivity.DEBUG_TAG,"Get data from span");
				cableName = s.getString(s.getColumnIndexOrThrow(KEY_CABLENAME));
				deviceName = s.getString(s.getColumnIndexOrThrow(KEY_DEVICENAME));
				length = s.getDouble(s.getColumnIndexOrThrow(KEY_DISTANCE));
				Log.v(SysLvlActivity.DEBUG_TAG,"cableName:"+cableName+", deviceName:"+deviceName+", length:"+length);
				
				Log.v(SysLvlActivity.DEBUG_TAG,"Get data from cable");
				Cursor c = fetchSingleCable(cableName);				
				cableLossRateLow = c.getDouble(c.getColumnIndexOrThrow("mhz55"));
				cableLossRateHigh = c.getDouble(c.getColumnIndexOrThrow("mhz550"));
				Log.v(SysLvlActivity.DEBUG_TAG,"cableLossLow:"+cableLossRateLow+", cableLossHigh:"+cableLossRateHigh);
				
				Log.v(SysLvlActivity.DEBUG_TAG,"Get data from Device");
				Cursor t = fetchSingleDevice(deviceName);				
				tapLossLow = t.getDouble(t.getColumnIndexOrThrow("tapmhz55"));
				tapLossHigh = t.getDouble(t.getColumnIndexOrThrow("tapmhz550"));
				hotLossLow = t.getDouble(t.getColumnIndexOrThrow("hotmhz55"));
				hotLossHigh = t.getDouble(t.getColumnIndexOrThrow("hotmhz550"));
				Log.v(SysLvlActivity.DEBUG_TAG,"deviceTapLossLow:"+tapLossLow+", deviceTapLossHigh:"+tapLossHigh+", deviceHotLossLow:"+hotLossLow+", deviceHotLossHigh:"+hotLossHigh);
				
				Log.v(SysLvlActivity.DEBUG_TAG, "Run Calculations");
				double cableLossLow = calcLoss(length, cableLossRateLow);
				double cableLossHigh = calcLoss(length, cableLossRateHigh);
				
				double tapOutputLow = incomingSignalLow - cableLossLow - tapLossLow;
				double tapOutputHigh = incomingSignalHigh - cableLossHigh - tapLossHigh;
				double hotOutputLow = incomingSignalLow - cableLossLow - hotLossLow;
				double hotOutputHigh = incomingSignalHigh - cableLossHigh - hotLossHigh;
				
				long rowId = fetchSingleRowId(i);
				
				double roundedTapOutputHigh = round(tapOutputHigh,2,BigDecimal.ROUND_HALF_UP);
				double roundedTapOutputLow = round(tapOutputLow,2,BigDecimal.ROUND_HALF_UP);
				double roundedHotOutputHigh = round(hotOutputHigh,2,BigDecimal.ROUND_HALF_UP);
				double roundedHotOutputLow = round(hotOutputLow,2,BigDecimal.ROUND_HALF_UP);
				
				Log.v(SysLvlActivity.DEBUG_TAG,"updateSingleSpan - TapHigh: "+roundedTapOutputHigh+", TapLow: "+roundedTapOutputLow+", HotHigh: "+roundedHotOutputHigh+", HotLow: "+roundedHotOutputLow);
				updateSingleSpan(rowId, i, (int)length, cableName, deviceName, roundedTapOutputHigh, roundedTapOutputLow, roundedHotOutputHigh, roundedHotOutputLow);
				
				incomingSignalHigh = roundedHotOutputHigh;
				incomingSignalLow = roundedHotOutputLow;
				
				s.moveToNext();
			}

		}
	}

	/**
	 * Move Functions
	 */
	public Boolean moveSpanUp(long position) throws SQLException {
		Log.v(SysLvlActivity.DEBUG_TAG, "start moveSpanUp");
		Boolean result = null;
		try {

			if (position != 0) {
				long rowId1 = fetchSingleRowId(position);
				long rowId2 = fetchSingleRowId(position - 1);
				Log.v(SysLvlActivity.DEBUG_TAG, "position: " + position
						+ ", rowId1: " + rowId1 + ", rowId2: " + rowId2);

				String sql1 = "UPDATE OR ABORT " + SPANS_TABLE + " SET "
						+ KEY_POSITION + " = " + (position) + " WHERE "
						+ KEY_ROWID + " = " + rowId2;
				Log.v(SysLvlActivity.DEBUG_TAG, sql1);
				database.execSQL(sql1);

				String sql2 = "UPDATE OR ABORT " + SPANS_TABLE + " SET "
						+ KEY_POSITION + " = " + (position - 1) + " WHERE "
						+ KEY_ROWID + " = " + rowId1;
				Log.v(SysLvlActivity.DEBUG_TAG, sql2);
				database.execSQL(sql2);

				Log.v(SysLvlActivity.DEBUG_TAG,
						"moveSpanUp completed successfully.");

				result = true;
			} else {
				Log.v(SysLvlActivity.DEBUG_TAG,
						"Span already at the top, aborting");
				result = false;
			}

		} catch (SQLException e) {
			Log.e(SysLvlActivity.DEBUG_TAG, "Failed to move span up.");
			Log.e(SysLvlActivity.DEBUG_TAG, "Error: " + e.toString());
		}
		Log.v(SysLvlActivity.DEBUG_TAG, "moveSpanUp result: " + result);
		return result;
	}

	public Boolean moveSpanDown(long position) throws SQLException {
		Log.v(SysLvlActivity.DEBUG_TAG, "start moveSpanDown");

		Boolean result = null;
		try {
			long totalSpans = fetchSpanCount();

			if (position < totalSpans - 1) {
				long rowId1 = fetchSingleRowId(position);
				long rowId2 = fetchSingleRowId(position + 1);
				Log.v(SysLvlActivity.DEBUG_TAG, "position: " + position
						+ ", rowId1: " + rowId1 + ", rowId2: " + rowId2);

				String sql1 = "UPDATE OR ABORT " + SPANS_TABLE + " SET "
						+ KEY_POSITION + " = " + (position) + " WHERE "
						+ KEY_ROWID + " = " + rowId2;
				Log.v(SysLvlActivity.DEBUG_TAG, sql1);
				database.execSQL(sql1);

				String sql2 = "UPDATE OR ABORT " + SPANS_TABLE + " SET "
						+ KEY_POSITION + " = " + (position + 1) + " WHERE "
						+ KEY_ROWID + " = " + rowId1;
				Log.v(SysLvlActivity.DEBUG_TAG, sql2);
				database.execSQL(sql2);

				Log.v(SysLvlActivity.DEBUG_TAG,
						"moveSpanDown completed successfully.");

				result = true;
			} else {
				Log.v(SysLvlActivity.DEBUG_TAG,
						"Span already at the top, aborting");
				result = false;
			}

		} catch (SQLException e) {
			Log.e(SysLvlActivity.DEBUG_TAG, "Failed to move span down.");
			Log.e(SysLvlActivity.DEBUG_TAG, "Error: " + e.toString());
		}
		return result;
	}
	
	/**
	 * Delete Functions
	 */
	public boolean deleteSingleSpan(long rowId) {
		Log.v(SysLvlActivity.DEBUG_TAG, "deleteSpan");
		return database.delete(SPANS_TABLE, KEY_ROWID + " = " + rowId, null) > 0;
	}

	/**
	 * Other Functions
	 */
	private double calcLoss(double length, double rateOfLoss){
		double loss = 0;
		loss = length * rateOfLoss / 100;
		return loss;
	}
	
	private ContentValues createContentValues(int distance, String cableName,
			String deviceName, double tapHigh, double tapLow, double hotHigh,
			double hotLow) {

		ContentValues values = new ContentValues();
		values.put("distance", distance);
		values.put("cableName", cableName);
		values.put("deviceName", deviceName);
		values.put("tapHigh", tapHigh);
		values.put("tapLow", tapLow);
		values.put("hotHigh", hotHigh);
		values.put("hotLow", hotLow);
		return values;

	}

	public static double round(double unrounded, int precision, int roundingMode)
	{
	    BigDecimal bd = new BigDecimal(unrounded);
	    BigDecimal rounded = bd.setScale(precision, roundingMode);
	    return rounded.doubleValue();
	}
}
