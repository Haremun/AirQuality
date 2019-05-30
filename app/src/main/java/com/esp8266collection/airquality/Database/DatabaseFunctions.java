package com.esp8266collection.airquality.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esp8266collection.airquality.DataParser;
import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Sensors.SensorsCollection;
import com.esp8266collection.airquality.UpdateData;

public class DatabaseFunctions {

    private SQLiteHelper helper;

    public DatabaseFunctions(SQLiteHelper helper) {
        this.helper = helper;
    }

    public void addToDatabase(UpdateData updateData) {

        SensorsCollection collection = updateData.getSensorsCollection();

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseStructure.TABLE_NAME_TEMPERATURE,
                collection.getStringSensorValue(SensorName.TemperatureSensor));
        contentValues.put(DatabaseStructure.TABLE_NAME_AIRQ,
                collection.getStringSensorValue(SensorName.AirQSensor));
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_25,
                collection.getSensorValue(SensorName.DustSensor25));
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_10,
                collection.getSensorValue(SensorName.DustSensor10));
        contentValues.put(DatabaseStructure.TABLE_NAME_BATTERY,
                collection.getSensorValue(SensorName.BatterySensor));
        contentValues.put(DatabaseStructure.TABLE_NAME_TIME, updateData.getCalendar().getTimeInMillis());

        db.insert(DatabaseStructure.TABLE_NAME, null, contentValues);
    }

    public UpdateData getLastUpdate() {
        UpdateData updateData;

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseStructure.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "id DESC"
        );

        if (cursor.moveToFirst()) {
            DataParser dataParser = new DataParser();
            SensorsCollection sensorsCollection = dataParser.parseCursor(cursor);

            updateData = new UpdateData(sensorsCollection, dataParser.getCalendar());

        } else {
            updateData = null;
        }

        cursor.close();
        return updateData;
    }

    public Cursor getFromDatabase(String limit) {

        SQLiteDatabase db = helper.getReadableDatabase();

        return db.query(
                DatabaseStructure.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "id DESC",
                limit
        );

    }
}
