package com.esp8266collection.airquality;

import com.esp8266collection.airquality.Enums.SensorName;

import java.util.LinkedList;
import java.util.List;

class SensorsCollection {
    private List<Sensor> sensors;

    SensorsCollection() {
        sensors = new LinkedList<>();
    }

    public void addSensor(SensorName sensorName) {
        sensors.add(new Sensor(sensorName));
    }
    public Sensor getSensor(SensorName sensorName){
        for(int i = 0; i < sensors.size(); i++){
            Sensor temp = sensors.get(i);
            if(temp.getSensorName().equals(sensorName))
                return temp;
        }
        return null;
    }

    public void updateSensor(SensorName sensorName, String value){
        for(int i = 0; i < sensors.size(); i++){
            Sensor temp = sensors.get(i);
            if(temp.getSensorName().equals(sensorName))
                temp.setValueFromString(value);
        }
    }

    public String getSensorValue(SensorName sensorName){
        for(int i = 0; i < sensors.size(); i++){
            Sensor temp = sensors.get(i);
            if(temp.getSensorName().equals(sensorName))
                return temp.getValue();
        }
            return null;
    }
}
