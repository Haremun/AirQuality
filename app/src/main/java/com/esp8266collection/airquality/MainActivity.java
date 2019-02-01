package com.esp8266collection.airquality;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements UpdateCallback {

    TextView textTemp;
    TextView textAirQ;
    TextView textDust;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTemp = findViewById(R.id.textTemp);
        textAirQ = findViewById(R.id.textAirQ);
        textDust = findViewById(R.id.textDust);

        ServerConnectionThread serverConnectionThread = new ServerConnectionThread(this);
        serverConnectionThread.start();


    }

    @Override
    public void Update(final String[] parts) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTemp.setText(parts[0]);
                textAirQ.setText(parts[1]);
                textDust.setText(parts[2]);

            }
        });
    }
}
