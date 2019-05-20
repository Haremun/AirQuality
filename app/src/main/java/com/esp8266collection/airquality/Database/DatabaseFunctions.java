package com.esp8266collection.airquality.Database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseFunctions {

    private SQLiteHelper helper;

    public DatabaseFunctions(SQLiteHelper helper) {
        this.helper = helper;
    }

    public void addToDatabase(int pm25, int pm10) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_25, pm25);
        contentValues.put(DatabaseStructure.TABLE_NAME_PM_10, pm10);

        db.insert(DatabaseStructure.TABLE_NAME, null, contentValues);
    }

    public Cursor getFromDatabase(String limit) {

        SQLiteDatabase db = helper.getReadableDatabase();

        return  db.query(
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
