package com.StupidRat.SysLvl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

@RunWith(LenientRobolectricTestRunner.class)
@Config(sdk = 33)
public class SysLvlDbAdapterTest {

    private static final String DATABASE_NAME = "SysLvlDB";

    private SharedPreferences mockPrefs;
    private Context testContext;
    private SysLvlDbAdapter adapter;

    @Before
    public void setUp() {
        Context baseContext = RuntimeEnvironment.getApplication();
        baseContext.deleteDatabase(DATABASE_NAME);

        mockPrefs = mock(SharedPreferences.class);
        when(mockPrefs.getInt(eq("HighFreqOutput"), anyInt())).thenReturn(45);
        when(mockPrefs.getInt(eq("LowFreqOutput"), anyInt())).thenReturn(36);

        testContext = new ContextWrapper(baseContext) {
            @Override
            public SharedPreferences getSharedPreferences(String name, int mode) {
                if (SysLvlActivity.SYSLVL_PREFS.equals(name)) {
                    return mockPrefs;
                }
                return super.getSharedPreferences(name, mode);
            }
        };

        adapter = new SysLvlDbAdapter(testContext);
        adapter.open();
    }

    @After
    public void tearDown() {
        if (adapter != null) {
            adapter.close();
        }
    }

    @Test
    public void updateAllSpanAttenuation_updatesSampleSpanOutputsWithLaunchLevels() {
        adapter.updateAllSpanAttenuation();

        Cursor cursor = adapter.fetchAllSpans();
        Map<Integer, double[]> actualByPosition = new HashMap<Integer, double[]>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    int position = cursor.getInt(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_POSITION));
                    double tapHigh = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_TAPHIGH));
                    double tapLow = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_TAPLOW));
                    double hotHigh = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_HOTHIGH));
                    double hotLow = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_HOTLOW));
                    actualByPosition.put(position, new double[] {tapHigh, tapLow, hotHigh, hotLow});
                } while (cursor.moveToNext());
            }
        } finally {
            cursor.close();
        }

        assertEquals("Expected five sample spans", 5, actualByPosition.size());

        assertDoubleArrayEquals(new double[] {17.5, 9.54, 42.7, 35.24}, actualByPosition.get(0));
        assertDoubleArrayEquals(new double[] {15.2, 8.78, 40.4, 34.48}, actualByPosition.get(1));
        assertDoubleArrayEquals(new double[] {18.9, 14.02, 37.8, 33.32}, actualByPosition.get(2));
        assertDoubleArrayEquals(new double[] {19.3, 15.86, 35.2, 32.26}, actualByPosition.get(3));
        assertDoubleArrayEquals(new double[] {19.7, 17.8, 32.1, 30.7}, actualByPosition.get(4));
    }

    private void assertDoubleArrayEquals(double[] expected, double[] actual) {
        assertTrue("Missing span outputs for expected position", actual != null);
        assertArrayEquals("Stored span outputs should match expected attenuation", expected, actual, 0.01);
    }
}
