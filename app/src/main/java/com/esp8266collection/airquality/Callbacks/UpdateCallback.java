package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.SensorsCollection;

public interface UpdateCallback {
    void Update(SensorsCollection sensorsCollection, String date);
}
