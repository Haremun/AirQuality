package com.esp8266collection.airquality.Database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import com.esp8266collection.airquality.Callbacks.DatabaseCallback;
import com.esp8266collection.airquality.Database.DatabaseStructure;
import com.esp8266collection.airquality.Database.SQLiteHelper;
import com.esp8266collection.airquality.DustValues;

import java.util.ArrayList;
import java.util.List;

public class LoadFromDatabaseTask extends AsyncTask<Void, Void, Void> {

    private DatabaseCallback databaseCallback;
    private List<DustValues> dustValuesList;
    private SQLiteHelper helper;


    public LoadFromDatabaseTask(SQLiteHelper helper, DatabaseCallback databaseCallback) {
        this.helper = helper;
        this.databaseCallback = databaseCallback;

        this.dustValuesList = new ArrayList<>();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseStructure.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                "id DESC",
                "12"
        );

        while (cursor.moveToNext()) {
            DustValues dustValues =
                    new DustValues(cursor.getInt(1), cursor.getInt(2));
            dustValuesList.add(dustValues);
        }
        cursor.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        databaseCallback.onDatabaseLoad(dustValuesList);
    }
}
