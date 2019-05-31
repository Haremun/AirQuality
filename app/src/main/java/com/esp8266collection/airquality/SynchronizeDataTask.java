package com.esp8266collection.airquality;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.esp8266collection.airquality.Database.DatabaseFunctions;
import com.esp8266collection.airquality.Database.SQLiteHelper;
import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.util.Calendar;

public class SynchronizeDataTask extends AsyncTask<Void, Void, Void> {

    private static final String SCRIPT_URL = "http://esp8266collection.keep.pl/json/get_last_12.php";
    private static final int UPDATES_LIMIT = 12;
    private UpdateData[] serverUpdates;
    private UpdateData[] databaseUpdates;
    private DataParser dataParser;
    private SQLiteHelper helper;

    public SynchronizeDataTask(SQLiteHelper helper) {
        serverUpdates = new UpdateData[UPDATES_LIMIT];
        databaseUpdates = new UpdateData[UPDATES_LIMIT];
        dataParser = new DataParser();
        this.helper = helper;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.connect(SCRIPT_URL);
        DatabaseFunctions databaseFunctions = new DatabaseFunctions(helper);

        Cursor cursor = databaseFunctions.getFromDatabase("12");

        if (serverConnection.isConnected()) {
            for (int i = 0; i < UPDATES_LIMIT; i++) {

                String response = serverConnection.getResponse(); //Load row from server
                if (response != null) {
                    SensorsCollection collection = new SensorsCollection();
                    collection.initializeSensors();
                    dataParser.parseString(response, collection);
                    //Log.i("Result_test", collection.getStringSensorValue(SensorName.DustSensor25));
                    serverUpdates[i] = new UpdateData(collection, dataParser.getCalendar());
                }
                if (cursor.moveToNext()) { //Load row from local database
                    SensorsCollection collection = new SensorsCollection();
                    collection.initializeSensors();
                    dataParser.parseCursor(cursor, collection);
                    //Log.i("Result_test", collection.getStringSensorValue(SensorName.DustSensor25));
                    databaseUpdates[i] = new UpdateData(collection, dataParser.getCalendar());
                }
            }
        }


        UpdateData[] result = new UpdateData[UPDATES_LIMIT];
        int index_server = 0;
        int index_db = 0;
        UpdateData serverData = null;
        Calendar serverDataCalendar = null;
        UpdateData localData = null;
        Calendar localDataCalendar = null;

        for (int k = 0; k < UPDATES_LIMIT; k++) {
            try {
                serverData = serverUpdates[index_server];
                serverDataCalendar = serverData.getCalendar();
                localData = databaseUpdates[index_db];
                localDataCalendar = localData.getCalendar();

                if (serverDataCalendar.compareTo(localDataCalendar) > 0) {
                    result[k] = serverData;
                    index_server++;
                } else if (serverDataCalendar.compareTo(localDataCalendar) < 0) {
                    result[k] = localData;
                    index_db++;
                } else {
                    result[k] = serverData;
                    index_server++;
                    index_db++;
                }
            } catch (NullPointerException e) { //If there is no data
                if (serverDataCalendar == null && localDataCalendar == null)
                    k = UPDATES_LIMIT; //End loop
                else if (serverDataCalendar == null) {
                    result[k] = localData;
                    index_db++;
                } else if (localDataCalendar == null) {
                    result[k] = serverData;
                    index_server++;
                }

            }

        }
        for (int i = UPDATES_LIMIT - 1; i >= 0; i--) {
            UpdateData data = result[i];
            if (data != null)
                databaseFunctions.addToDatabase(data);
        }


        return null;
    }
}
