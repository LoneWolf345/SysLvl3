package com.StupidRat.SysLvl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.database.Cursor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(LenientRobolectricTestRunner.class)
@Config(sdk = 33)
public class GeoNotesDbAdapterTest {

    private static final String DATABASE_NAME = "data";

    private GeoNotesDbAdapter adapter;
    private Context context;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.getApplication();
        context.deleteDatabase(DATABASE_NAME);
        adapter = new GeoNotesDbAdapter(context);
        adapter.open();
    }

    @After
    public void tearDown() {
        if (adapter != null) {
            adapter.close();
        }
    }

    @Test
    public void createUpdateDeleteNote_roundTripPersistsAllColumns() {
        long rowId = adapter.createNote("Title", "Body", "51.123", "-0.123", "123 Main", "CA", "90210");
        assertTrue("Create should return a valid row id", rowId > 0);

        Cursor createdCursor = adapter.fetchNote(rowId);
        try {
            assertEquals("Title", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_TITLE)));
            assertEquals("Body", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_BODY)));
            assertEquals("51.123", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LAT)));
            assertEquals("-0.123", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LONG)));
            assertEquals("123 Main", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STREET)));
            assertEquals("CA", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STATE)));
            assertEquals("90210", createdCursor.getString(createdCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_ZIP)));
        } finally {
            createdCursor.close();
        }

        boolean updated = adapter.updateNote(rowId, "Updated", "Body 2", "51.5", "-0.5", "456 Elm", "WA", "98101");
        assertTrue("Update should report success", updated);

        Cursor updatedCursor = adapter.fetchNote(rowId);
        try {
            assertEquals("Updated", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_TITLE)));
            assertEquals("Body 2", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_BODY)));
            assertEquals("51.5", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LAT)));
            assertEquals("-0.5", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LONG)));
            assertEquals("456 Elm", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STREET)));
            assertEquals("WA", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STATE)));
            assertEquals("98101", updatedCursor.getString(updatedCursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_ZIP)));
        } finally {
            updatedCursor.close();
        }

        Cursor allCursor = adapter.fetchAllNotes();
        try {
            assertEquals(1, allCursor.getCount());
        } finally {
            allCursor.close();
        }

        boolean deleted = adapter.deleteNote(rowId);
        assertTrue("Delete should remove the note", deleted);

        Cursor emptyCursor = adapter.fetchAllNotes();
        try {
            assertEquals(0, emptyCursor.getCount());
        } finally {
            emptyCursor.close();
        }
    }
}
