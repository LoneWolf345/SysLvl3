package com.StupidRat.SysLvl.legacy.data;

import android.content.Context;
import android.database.Cursor;

import com.StupidRat.SysLvl.SysLvlDbAdapter;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LegacySpanRepository implements SpanRepository {
    private final SysLvlDbAdapter adapter;
    private final CopyOnWriteArrayList<Observer> observers = new CopyOnWriteArrayList<Observer>();
    private List<Span> cachedSpans = new ArrayList<Span>();

    public LegacySpanRepository(Context context) throws SQLException {
        adapter = new SysLvlDbAdapter(context.getApplicationContext());
        adapter.open();
        refresh();
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
            observer.onSpansChanged(getSpans());
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public List<Span> getSpans() {
        return Collections.unmodifiableList(cachedSpans);
    }

    @Override
    public long getSpanIdAtPosition(long position) {
        for (Span span : cachedSpans) {
            if (span.getPosition() == position) {
                return span.getId();
            }
        }
        return -1L;
    }

    @Override
    public void createSpan(int distance, String cableName, String deviceName,
                           double tapHigh, double tapLow, double hotHigh, double hotLow) throws SQLException {
        adapter.createSpan(distance, cableName, deviceName, tapHigh, tapLow, hotHigh, hotLow);
        refresh();
    }

    @Override
    public void moveSpanUp(long position) throws SQLException {
        adapter.moveSpanUp(position);
        refresh();
    }

    @Override
    public void moveSpanDown(long position) throws SQLException {
        adapter.moveSpanDown(position);
        refresh();
    }

    @Override
    public void deleteSpan(long spanId) throws SQLException {
        adapter.deleteSingleSpan(spanId);
        refresh();
    }

    @Override
    public void updateSpanPositions() throws SQLException {
        adapter.updateAllSpanPositions();
        refresh();
    }

    @Override
    public void updateAllSpanAttenuation() throws SQLException {
        adapter.updateAllSpanAttenuation();
        refresh();
    }

    @Override
    public final void refresh() throws SQLException {
        Cursor cursor = null;
        List<Span> spans = new ArrayList<Span>();
        try {
            cursor = adapter.fetchAllSpans();
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    spans.add(mapSpan(cursor));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        cachedSpans = spans;
        notifyObservers();
    }

    @Override
    public void close() {
        adapter.close();
        observers.clear();
        cachedSpans = Collections.emptyList();
    }

    private void notifyObservers() {
        List<Span> snapshot = getSpans();
        for (Observer observer : observers) {
            observer.onSpansChanged(snapshot);
        }
    }

    private Span mapSpan(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_ROWID));
        int position = cursor.getInt(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_POSITION));
        int distance = cursor.getInt(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_DISTANCE));
        String cableName = cursor.getString(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_CABLENAME));
        String deviceName = cursor.getString(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_DEVICENAME));
        double tapHigh = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_TAPHIGH));
        double tapLow = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_TAPLOW));
        double hotHigh = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_HOTHIGH));
        double hotLow = cursor.getDouble(cursor.getColumnIndexOrThrow(SysLvlDbAdapter.KEY_HOTLOW));
        return new Span(id, position, distance, cableName, deviceName, tapHigh, tapLow, hotHigh, hotLow);
    }
}
