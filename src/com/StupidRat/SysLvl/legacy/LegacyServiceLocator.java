package com.StupidRat.SysLvl.legacy;

import android.content.Context;

import com.StupidRat.SysLvl.legacy.data.GeoNoteRepository;
import com.StupidRat.SysLvl.legacy.data.LegacyGeoNoteRepository;
import com.StupidRat.SysLvl.legacy.data.LegacySpanRepository;
import com.StupidRat.SysLvl.legacy.data.SpanRepository;

import java.sql.SQLException;

public final class LegacyServiceLocator {
    public interface SpanRepositoryFactory {
        SpanRepository create(Context context) throws SQLException;
    }

    public interface GeoNoteRepositoryFactory {
        GeoNoteRepository create(Context context) throws SQLException;
    }

    private static SpanRepositoryFactory spanRepositoryFactory = new SpanRepositoryFactory() {
        @Override
        public SpanRepository create(Context context) throws SQLException {
            return new LegacySpanRepository(context);
        }
    };

    private static GeoNoteRepositoryFactory geoNoteRepositoryFactory = new GeoNoteRepositoryFactory() {
        @Override
        public GeoNoteRepository create(Context context) throws SQLException {
            return new LegacyGeoNoteRepository(context);
        }
    };

    private LegacyServiceLocator() {
    }

    public static SpanRepository provideSpanRepository(Context context) throws SQLException {
        return spanRepositoryFactory.create(context.getApplicationContext());
    }

    public static void setSpanRepositoryFactory(SpanRepositoryFactory factory) {
        if (factory != null) {
            spanRepositoryFactory = factory;
        }
    }

    public static GeoNoteRepository provideGeoNoteRepository(Context context) throws SQLException {
        return geoNoteRepositoryFactory.create(context.getApplicationContext());
    }

    public static void setGeoNoteRepositoryFactory(GeoNoteRepositoryFactory factory) {
        if (factory != null) {
            geoNoteRepositoryFactory = factory;
        }
    }
}
