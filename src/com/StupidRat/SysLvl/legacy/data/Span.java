package com.StupidRat.SysLvl.legacy.data;

public class Span {
    private final long id;
    private final int position;
    private final int distance;
    private final String cableName;
    private final String deviceName;
    private final double tapHigh;
    private final double tapLow;
    private final double hotHigh;
    private final double hotLow;

    public Span(long id, int position, int distance, String cableName, String deviceName,
                double tapHigh, double tapLow, double hotHigh, double hotLow) {
        this.id = id;
        this.position = position;
        this.distance = distance;
        this.cableName = cableName;
        this.deviceName = deviceName;
        this.tapHigh = tapHigh;
        this.tapLow = tapLow;
        this.hotHigh = hotHigh;
        this.hotLow = hotLow;
    }

    public long getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }

    public int getDistance() {
        return distance;
    }

    public String getCableName() {
        return cableName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public double getTapHigh() {
        return tapHigh;
    }

    public double getTapLow() {
        return tapLow;
    }

    public double getHotHigh() {
        return hotHigh;
    }

    public double getHotLow() {
        return hotLow;
    }
}
