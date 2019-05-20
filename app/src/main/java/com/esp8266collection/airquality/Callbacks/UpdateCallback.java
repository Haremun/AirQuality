package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.Sensors.SensorsCollection;

public interface UpdateCallback {
    void Update(SensorsCollection sensorsCollection, String date);
    void onConnectionError();
}
