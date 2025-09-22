package com.StupidRat.SysLvl.legacy.data;

import android.content.Context;
import android.database.Cursor;

import com.StupidRat.SysLvl.GeoNotesDbAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LegacyGeoNoteRepository implements GeoNoteRepository {
    private final GeoNotesDbAdapter adapter;
    private final CopyOnWriteArrayList<Observer> observers = new CopyOnWriteArrayList<Observer>();
    private List<GeoNote> cachedNotes = new ArrayList<GeoNote>();

    public LegacyGeoNoteRepository(Context context) throws SQLException {
        adapter = new GeoNotesDbAdapter(context.getApplicationContext());
        adapter.open();
        refresh();
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            observer.onNotesChanged(getNotes());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public List<GeoNote> getNotes() {
        return Collections.unmodifiableList(cachedNotes);
    }

    @Override
    public GeoNote getNote(long id) throws SQLException {
        Cursor cursor = null;
        try {
            cursor = adapter.fetchNote(id);
            if (cursor != null && cursor.moveToFirst()) {
                return mapNote(cursor);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    @Override
    public long createNote(String title, String body, String latitude, String longitude,
                           String street, String state, String zip) throws SQLException {
        long id = adapter.createNote(title, body, latitude, longitude, street, state, zip);
        refresh();
        return id;
    }

    @Override
    public boolean deleteNote(long id) throws SQLException {
        boolean deleted = adapter.deleteNote(id);
        refresh();
        return deleted;
    }

    @Override
    public boolean updateNote(long id, String title, String body, String latitude, String longitude,
                              String street, String state, String zip) throws SQLException {
        boolean updated = adapter.updateNote(id, title, body, latitude, longitude, street, state, zip);
        refresh();
        return updated;
    }

    @Override
    public final void refresh() throws SQLException {
        Cursor cursor = null;
        List<GeoNote> notes = new ArrayList<GeoNote>();
        try {
            cursor = adapter.fetchAllNotes();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    notes.add(mapNote(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        cachedNotes = notes;
        notifyObservers();
    }

    @Override
    public void close() {
        adapter.close();
        observers.clear();
        cachedNotes = Collections.emptyList();
    }

    private void notifyObservers() {
        List<GeoNote> snapshot = getNotes();
        for (Observer observer : observers) {
            observer.onNotesChanged(snapshot);
        }
    }

    private GeoNote mapNote(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_ROWID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_TITLE));
        String body = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_BODY));
        String latitude = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LAT));
        String longitude = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_LONG));
        String street = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STREET));
        String state = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_STATE));
        String zip = cursor.getString(cursor.getColumnIndexOrThrow(GeoNotesDbAdapter.KEY_ZIP));
        return new GeoNote(id, title, body, latitude, longitude, street, state, zip);
    }
}
