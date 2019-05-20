package com.esp8266collection.airquality;

public class DustValues {
    private int pm25;
    private int pm10;

    public DustValues(int pm25, int pm10){
        this.pm25 = pm25;
        this.pm10 = pm10;
    }

    public int getPm10() {
        return pm10;
    }

    public int getPm25() {
        return pm25;
    }
}
