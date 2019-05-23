package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetLastUpdateFromServer extends Thread {

    private UpdateCallback updateCallback;
    private boolean run = true;
    private DataParser dataParser;

    GetLastUpdateFromServer(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;

        this.dataParser = new DataParser();
    }

    @Override
    public void run() {
        while (true) {
            if (run) {
                try {
                    URL url = new URL("http://esp8266collection.keep.pl/json/get_data.php");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    if (checkConnection(connection)) {
                        InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder total = new StringBuilder();
                        for (String line; (line = r.readLine()) != null; ) {
                            total.append(line).append('\n');
                        }

                        SensorsCollection sensorsCollection = dataParser.parseString(total.toString());

                        updateCallback.Update(new UpdateData(sensorsCollection, dataParser.getCalendar()));

                    } else {
                        updateCallback.onConnectionError();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }

    private boolean checkConnection(HttpURLConnection connection) {
        try {
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void setRun(boolean run) {
        this.run = run;
    }
}
