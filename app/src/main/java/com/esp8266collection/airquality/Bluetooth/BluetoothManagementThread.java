package com.esp8266collection.airquality.Bluetooth;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.DataParser;
import com.esp8266collection.airquality.Sensors.SensorsCollection;
import com.esp8266collection.airquality.UpdateData;

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
    private BluetoothSocket bluetoothSocket;

    private UpdateCallback updateCallback;

    public BluetoothManagementThread(BluetoothSocket socket, UpdateCallback updateCallback) {
        Log.i("BluetoothTest", "Connected");

        this.bluetoothSocket = socket;
        this.updateCallback = updateCallback;
        this.dataParser = new DataParser();

        connectAndGetStreams(socket);

    }

    @Override
    public void run() {
        try {
            while (true) {
                if (bluetoothSocket.isConnected()){
                    String line = reader.readLine();

                    Log.i("BluetoothTest", line);

                    SensorsCollection sensorsCollection = dataParser.parseString(line);

                    updateCallback.Update(new UpdateData(sensorsCollection, dataParser.getCalendar()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void closeConnection() {

        if (bluetoothSocket.isConnected())
            try {
                mInputStream.close();
                mOutputStream.flush();
                mOutputStream.close();
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    private void connectAndGetStreams(BluetoothSocket socket) {
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
