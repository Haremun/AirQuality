package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLastUpdateFromServer extends Thread {

    private UpdateCallback updateCallback;
    private boolean run = true;
    private DataParser dataParser;

    public GetLastUpdateFromServer(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;

        this.dataParser = new DataParser();
    }

    @Override
    public void run() {
        while (true) {
            if (run) {
                ServerConnection serverConnection = new ServerConnection();
                serverConnection.connect("http://esp8266collection.keep.pl/json/get_data.php");
                if (serverConnection.isConnected()){
                    String response = serverConnection.getResponse();
                    SensorsCollection sensorsCollection = dataParser.parseString(response);

                    updateCallback.Update(new UpdateData(sensorsCollection, dataParser.getCalendar()));

                    serverConnection.close();
                } else {
                    updateCallback.onConnectionError();
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }


    }


    public void setRun(boolean run) {
        this.run = run;
    }
}
