package com.esp8266collection.airquality;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class ServerConnectionThread extends Thread {

    UpdateCallback updateCallback;
    int i = 0;

    ServerConnectionThread(UpdateCallback updateCallback){
        this.updateCallback = updateCallback;
    }
    @Override
    public void run() {
        while(i < 100){
            try {
                URL url = new URL("http://esp8266collection.keep.pl/json/get_data.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder total = new StringBuilder();
                for (String line; (line = r.readLine()) != null; ) {
                    total.append(line).append('\n');
                }
                String string = total.toString();
                char symbol = '%';
                int index = string.indexOf(symbol);
                String data = string.substring(0, index);

                String[] parts = data.split("&");
                float number = Float.parseFloat(parts[0]);
                parts[2] = new DecimalFormat("##.##").format(number);
                updateCallback.Update(parts);

                Thread.sleep(5000);
                i++;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
