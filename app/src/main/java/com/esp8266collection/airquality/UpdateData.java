package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.util.Calendar;

public class UpdateData {
    private SensorsCollection sensorsCollection;
    private Calendar calendar;

    public UpdateData(SensorsCollection sensorsCollection, Calendar calendar) {
        this.sensorsCollection = sensorsCollection;
        this.calendar = calendar;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public SensorsCollection getSensorsCollection() {
        return sensorsCollection;
    }
}
