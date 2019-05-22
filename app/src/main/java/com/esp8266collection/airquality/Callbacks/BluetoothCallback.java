package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.BluetoothManagementThread;

public interface BluetoothCallback {
    void onBluetoothConnect(BluetoothManagementThread bluetoothManagementThread);
}
