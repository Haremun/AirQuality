package com.esp8266collection.airquality;

public class SettingsMessage {
    private String networkName = "";
    private String networkPassword = "";
    private int intervalIndex = -1;

    public SettingsMessage(){

    }

    public void setIntervalIndex(int intervalIndex) {
        this.intervalIndex = intervalIndex;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public void setNetworkPassword(String networkPassword) {
        this.networkPassword = networkPassword;
    }

    public String getNetworkName() {
        return networkName;
    }

    public int getIntervalIndex() {
        return intervalIndex;
    }

    public String getNetworkPassword() {
        return networkPassword;
    }
}
