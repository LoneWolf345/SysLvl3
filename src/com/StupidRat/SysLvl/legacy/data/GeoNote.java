package com.StupidRat.SysLvl.legacy.data;

public class GeoNote {
    private final long id;
    private final String title;
    private final String body;
    private final String latitude;
    private final String longitude;
    private final String street;
    private final String state;
    private final String zip;

    public GeoNote(long id, String title, String body, String latitude, String longitude,
                   String street, String state, String zip) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = street;
        this.state = state;
        this.zip = zip;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStreet() {
        return street;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }
}
