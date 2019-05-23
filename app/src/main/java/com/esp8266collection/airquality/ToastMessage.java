package com.esp8266collection.airquality;

public class ToastMessage {

    private int toastType;
    private String toastMessage;

    public ToastMessage(int toastType, String toastMessage) {
        this.toastType = toastType;
        this.toastMessage = toastMessage;
    }

    public int getToastType() {
        return toastType;
    }

    public String getToastMessage() {
        return toastMessage;
    }
}
