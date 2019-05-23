package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

public class DataParser {

    private SensorsCollection sensorsCollection;
    private String lastDate = "111";

    public DataParser() {
        sensorsCollection = new SensorsCollection();

        sensorsCollection.addSensor(SensorName.TemperatureSensor);
        sensorsCollection.addSensor(SensorName.AirQSensor);
        sensorsCollection.addSensor(SensorName.DustSensor25);
        sensorsCollection.addSensor(SensorName.DustSensor10);
    }

    public SensorsCollection parseString(String string) {
        char symbol = '%';
        int index = string.indexOf(symbol);

        if (index != -1)
            string = string.substring(0, index);

        String[] parts = string.split("&");
        sensorsCollection.updateSensor(SensorName.TemperatureSensor, parts[0]);
        sensorsCollection.getSensor(SensorName.TemperatureSensor).roundToUnits();
        sensorsCollection.updateSensor(SensorName.AirQSensor, parts[1]);
        sensorsCollection.updateSensor(SensorName.DustSensor25, parts[2]);
        sensorsCollection.updateSensor(SensorName.DustSensor10, parts[3]);

        if (parts.length > 4)
            lastDate = parts[4];

        return sensorsCollection;
    }

    public String getLastDate() {
        return lastDate;
    }
}
