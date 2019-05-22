package com.esp8266collection.airquality;

import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BluetoothManagementThread extends Thread{

    private InputStream mInputStream;
    private OutputStream mOutputStream;

    public BluetoothManagementThread(BluetoothSocket socket) {
        Log.i("BluetoothTest", "Connected");
        try {
            socket.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mInputStream = socket.getInputStream();
            mOutputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            byte[] temp = "1\n".getBytes();
            mOutputStream.write(temp);
            Log.i("BluetoothTest", temp[0] + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
