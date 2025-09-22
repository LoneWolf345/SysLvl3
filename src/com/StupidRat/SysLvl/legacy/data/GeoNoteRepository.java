package com.StupidRat.SysLvl.legacy.data;

import java.sql.SQLException;
import java.util.List;

public interface GeoNoteRepository {
    interface Observer {
        void onNotesChanged(List<GeoNote> notes);
    }

    void addObserver(Observer observer);

    void removeObserver(Observer observer);

    List<GeoNote> getNotes();

    GeoNote getNote(long id) throws SQLException;

    long createNote(String title, String body, String latitude, String longitude,
                    String street, String state, String zip) throws SQLException;

    boolean deleteNote(long id) throws SQLException;

    boolean updateNote(long id, String title, String body, String latitude, String longitude,
                       String street, String state, String zip) throws SQLException;

    void refresh() throws SQLException;

    void close();
}
