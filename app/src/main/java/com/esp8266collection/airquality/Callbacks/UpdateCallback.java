package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.Sensors.SensorsCollection;
import com.esp8266collection.airquality.UpdateData;

import java.util.Calendar;

public interface UpdateCallback {
    void Update(UpdateData updateData);
    void onConnectionError();
}
