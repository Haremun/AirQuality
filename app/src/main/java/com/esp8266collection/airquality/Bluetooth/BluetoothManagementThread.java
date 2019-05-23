package com.esp8266collection.airquality.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.DataParser;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class BluetoothManagementThread extends Thread {

    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private BufferedReader reader;
    private DataParser dataParser;

    private UpdateCallback updateCallback;

    public BluetoothManagementThread(BluetoothSocket socket, UpdateCallback updateCallback) {
        Log.i("BluetoothTest", "Connected");

        this.updateCallback = updateCallback;
        this.dataParser = new DataParser();

        connectAndGetStreams(socket);

    }

    @Override
    public void run() {
        try {
            while (true) {
                String line =  reader.readLine();

                SensorsCollection sensorsCollection = dataParser.parseString(line);

                updateCallback.Update(sensorsCollection, dataParser.getLastDate());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void connectAndGetStreams(BluetoothSocket socket){
        try {
            socket.connect();

            mInputStream = socket.getInputStream();
            mOutputStream = socket.getOutputStream();

            reader = new BufferedReader(new InputStreamReader(mInputStream));
            dataParser = new DataParser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
