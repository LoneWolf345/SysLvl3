package com.StupidRat.SysLvl.legacy.data;

import java.sql.SQLException;
import java.util.List;

public interface SpanRepository {
    interface Observer {
        void onSpansChanged(List<Span> spans);
    }

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    List<Span> getSpans();

    long getSpanIdAtPosition(long position);

    void createSpan(int distance, String cableName, String deviceName,
                    double tapHigh, double tapLow, double hotHigh, double hotLow) throws SQLException;

    void moveSpanUp(long position) throws SQLException;

    void moveSpanDown(long position) throws SQLException;

    void deleteSpan(long spanId) throws SQLException;

    void updateSpanPositions() throws SQLException;

    void updateAllSpanAttenuation() throws SQLException;

    void refresh() throws SQLException;

    void close();
}
