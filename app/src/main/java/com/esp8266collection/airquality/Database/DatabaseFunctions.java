package com.esp8266collection.airquality.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.esp8266collection.airquality.DataParser;
import com.esp8266collection.airquality.Sensors.SensorsCollection;
import com.esp8266collection.airquality.UpdateData;

import java.util.Calendar;

public class DatabaseFunctions {

    private SQLiteHelper helper;

    public DatabaseFunctions(SQLiteHelper helper) {
        this.helper = helper;
    }

    public void addToDatabase(int temperature, int airq, int pm25, int pm10, long time) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseStructure.TABLE_NAME_TEMPERATURE, temperature);
        contentValues.put(DatabaseStructure.TABLE_NAME_AIRQ, airq);
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_25, pm25);
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_10, pm10);
        contentValues.put(DatabaseStructure.TABLE_NAME_TIME, time);

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
