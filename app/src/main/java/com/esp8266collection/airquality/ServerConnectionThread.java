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

public class ServerConnectionThread extends Thread {

    private UpdateCallback updateCallback;
    private boolean run = true;
    private SensorsCollection sensorsCollection;

    ServerConnectionThread(UpdateCallback updateCallback) {
        this.updateCallback = updateCallback;
        this.sensorsCollection = new SensorsCollection();

        sensorsCollection.addSensor(SensorName.TemperatureSensor);
        sensorsCollection.addSensor(SensorName.AirQSensor);
        sensorsCollection.addSensor(SensorName.DustSensor25);
        sensorsCollection.addSensor(SensorName.DustSensor10);
    }

    @Override
    public void run() {
        while (run) {
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
                    String string = total.toString();
                    char symbol = '%';
                    int index = string.indexOf(symbol);
                    String data = string.substring(0, index);

                    String[] parts = data.split("&");

                    sensorsCollection.updateSensor(SensorName.TemperatureSensor, parts[0]);
                    sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
                    sensorsCollection.updateSensor(SensorName.AirQSensor, parts[1]);
                    sensorsCollection.updateSensor(SensorName.DustSensor25, parts[2]);
                    sensorsCollection.updateSensor(SensorName.DustSensor10, parts[3]);

                    updateCallback.Update(sensorsCollection, parts[4]);
                } else {
                    updateCallback.onConnectionError();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
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
