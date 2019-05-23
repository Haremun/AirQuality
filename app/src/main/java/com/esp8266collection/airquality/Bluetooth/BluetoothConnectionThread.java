package com.esp8266collection.airquality.Bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import com.esp8266collection.airquality.Callbacks.BluetoothCallback;
import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.DataFragment;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class BluetoothConnectionThread extends Thread {

    private static final String DEVICE_NAME = "HC-05";

    private BluetoothCallback bluetoothCallback;
    private UpdateCallback updateCallback;
    private Context context;
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice bluetoothDevice;

    public BluetoothConnectionThread(Context context, DataFragment dataFragment) {
        this.bluetoothCallback = (BluetoothCallback) dataFragment;
        this.updateCallback = (UpdateCallback) dataFragment;
        this.context = context;

    }

    public BluetoothConnectionThread(Context context) {
        this.context = context;

    }

    @Override
    public void run() {

        findDevice();

        if(bluetoothDevice != null){
            Log.i("BluetoothTest", "Connecting...");
            BluetoothSocket tmp = null;
            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = bluetoothDevice.createRfcommSocketToServiceRecord(
                        UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"));

                if (tmp != null){
                    BluetoothManagementThread bluetoothManagementThread = new BluetoothManagementThread(tmp, updateCallback);
                    bluetoothManagementThread.start();
                    if(bluetoothCallback != null)
                        bluetoothCallback.onBluetoothConnect(bluetoothManagementThread);
                }

            } catch (IOException e) {
                Log.i("BluetoothTest", "Socket's create() method failed", e);
            }
        }

    }

    private void findDevice() {
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                Log.i("BluetoothTest", deviceName);
                //String deviceHardwareAddress = device.getAddress(); // MAC address
                if (deviceName.equals(DEVICE_NAME)) {
                    Log.i("BluetoothTest", "Found: " + deviceName);
                    bluetoothAdapter.cancelDiscovery();
                    bluetoothDevice = device;
                    //return;
                }
            }
        }

        /*bluetoothAdapter.startDiscovery();

        // Register for broadcasts when a device is discovered.
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(mReceiver, filter);*/
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    /*private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                if (deviceName.equals(DEVICE_NAME)) {
                    Log.i("BluetoothTest", "Found: " + deviceName);
                    bluetoothAdapter.cancelDiscovery();
                    bluetoothDevice = device;
                }

            }
        }
    };*/
}
