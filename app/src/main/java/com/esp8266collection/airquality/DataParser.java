package com.esp8266collection.airquality;

import android.database.Cursor;

import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DataParser {

    private SensorsCollection sensorsCollection;
    private Calendar calendar;

    public DataParser() {
        sensorsCollection = new SensorsCollection();

        sensorsCollection.addSensor(SensorName.TemperatureSensor);
        sensorsCollection.addSensor(SensorName.AirQSensor);
        sensorsCollection.addSensor(SensorName.DustSensor25);
        sensorsCollection.addSensor(SensorName.DustSensor10);
        sensorsCollection.addSensor(SensorName.BatterySensor);
    }

    public SensorsCollection parseString(String string) {
        char symbol = '%';
        int index = string.indexOf(symbol);

        if (index != -1)
            string = string.substring(0, index);

        String[] parts = string.split("&");
        sensorsCollection.updateSensor(SensorName.TemperatureSensor, parts[0]);
        sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
        sensorsCollection.updateSensor(SensorName.AirQSensor, parts[1]);
        sensorsCollection.updateSensor(SensorName.DustSensor25, parts[2]);
        sensorsCollection.updateSensor(SensorName.DustSensor10, parts[3]);
        sensorsCollection.updateSensor(SensorName.BatterySensor, parts[4]);

        if (parts.length > sensorsCollection.size()){
            try {
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
                calendar.setTime(simpleDateFormat.parse(parts[sensorsCollection.size()]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            calendar = Calendar.getInstance(TimeZone.getDefault());
        }


        return sensorsCollection;
    }
    public SensorsCollection parseString(String string, SensorsCollection sensorsCollection) {
        char symbol = '%';
        int index = string.indexOf(symbol);

        if (index != -1)
            string = string.substring(0, index);

        String[] parts = string.split("&");
        sensorsCollection.updateSensor(SensorName.TemperatureSensor, parts[0]);
        sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
        sensorsCollection.updateSensor(SensorName.AirQSensor, parts[1]);
        sensorsCollection.updateSensor(SensorName.DustSensor25, parts[2]);
        sensorsCollection.updateSensor(SensorName.DustSensor10, parts[3]);
        sensorsCollection.updateSensor(SensorName.BatterySensor, parts[4]);

        if (parts.length > sensorsCollection.size()){
            try {
                calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss", Locale.ENGLISH);
                calendar.setTime(simpleDateFormat.parse(parts[sensorsCollection.size()]));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            calendar = Calendar.getInstance(TimeZone.getDefault());
        }


        return null;
    }
    public SensorsCollection parseCursor(Cursor cursor){
        sensorsCollection.updateSensor(SensorName.TemperatureSensor, cursor.getString(1));
        sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
        sensorsCollection.updateSensor(SensorName.AirQSensor, cursor.getString(2));
        sensorsCollection.updateSensor(SensorName.DustSensor25, cursor.getString(3));
        sensorsCollection.updateSensor(SensorName.DustSensor10, cursor.getString(4));
        sensorsCollection.updateSensor(SensorName.BatterySensor, cursor.getString(5));

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(6));

        return sensorsCollection;
    }
    public SensorsCollection parseCursor(Cursor cursor, SensorsCollection sensorsCollection){
        sensorsCollection.updateSensor(SensorName.TemperatureSensor, cursor.getString(1));
        sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
        sensorsCollection.updateSensor(SensorName.AirQSensor, cursor.getString(2));
        sensorsCollection.updateSensor(SensorName.DustSensor25, cursor.getString(3));
        sensorsCollection.updateSensor(SensorName.DustSensor10, cursor.getString(4));
        sensorsCollection.updateSensor(SensorName.BatterySensor, cursor.getString(5));

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(cursor.getLong(6));

        return null;
    }

    public Calendar getCalendar() {
        return calendar;
    }
}
