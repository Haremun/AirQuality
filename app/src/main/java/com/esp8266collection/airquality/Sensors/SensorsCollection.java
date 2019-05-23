package com.esp8266collection.airquality.Sensors;

import com.esp8266collection.airquality.Enums.SensorName;

import java.util.LinkedList;
import java.util.List;

public class SensorsCollection {
    private List<Sensor> sensors;

    public SensorsCollection() {
        sensors = new LinkedList<>();
    }

    public void initializeSensors(){
        addSensor(SensorName.TemperatureSensor);
        addSensor(SensorName.AirQSensor);
        addSensor(SensorName.DustSensor25);
        addSensor(SensorName.DustSensor10);
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
                return temp.getStringValue();
        }
            return null;
    }
}
