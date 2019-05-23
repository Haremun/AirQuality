package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.Bluetooth.BluetoothManagementThread;

public interface BluetoothCallback {
    void onBluetoothConnect(BluetoothManagementThread bluetoothManagementThread);
}
