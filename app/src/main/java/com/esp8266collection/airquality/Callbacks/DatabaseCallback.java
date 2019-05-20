package com.esp8266collection.airquality.Callbacks;

import com.esp8266collection.airquality.DustValues;

import java.util.List;

public interface DatabaseCallback {
    void onDatabaseLoad(List<DustValues> dustValuesList);
}
