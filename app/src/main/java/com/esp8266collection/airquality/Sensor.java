package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Enums.SensorName;

import java.text.DecimalFormat;

public class Sensor {
    private SensorName sensorName;
    private float sensorValue;

    public Sensor(SensorName _sensorName) {
        this.sensorName = _sensorName;
        this.sensorValue = 0f;
    }

    public void setValueFromString(String value) {
        sensorValue = Float.parseFloat(value);
    }

    public String getValue(){
        return new DecimalFormat("###.##").format(sensorValue);
    }

    public SensorName getSensorName() {
        return sensorName;
    }
}
