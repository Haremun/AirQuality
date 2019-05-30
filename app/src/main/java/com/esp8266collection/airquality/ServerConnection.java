package com.esp8266collection.airquality;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConnection {

    private boolean isConnected = false;
    private HttpURLConnection connection;
    private BufferedReader reader;
    private InputStream inputStream;

    public ServerConnection() {

    }

    public void connect(String link) {
        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            isConnected = true;

            inputStream = new BufferedInputStream(connection.getInputStream());
            reader = new BufferedReader(new InputStreamReader(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
            isConnected = false;
        }
    }

    public String getResponse() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public void close() {
        try {
            inputStream.close();
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
