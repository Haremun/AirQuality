package com.esp8266collection.airquality;


import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.esp8266collection.airquality.Callbacks.AnimationCallback;
import com.esp8266collection.airquality.Callbacks.RotationCallback;
import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.Enums.ConnectionMode;
import com.esp8266collection.airquality.Enums.MainCircleData;
import com.esp8266collection.airquality.Enums.SensorName;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment
        implements UpdateCallback, RotationCallback, AnimationCallback {

    //Text views
    private TextView textTemp;
    private TextView textDust;
    private TextView textDust2;
    private TextView textUpdate;
    private TextView textInfo;
    //Image views
    private ImageView imgPollSmallCircle;
    private ImageView imgPollCircle;
    private ImageView imgCircle;
    private ImageView imgFrame;
    //Layouts
    private FrameLayout btnConnect;
    private ConstraintLayout mainCircleLayout;
    //Threads
    private RotationThread rotationThread;
    private ToastDrawerAnimation toastDrawerAnimation;

    private boolean connectionError = false;
    private MainCircleData mainCircleData = MainCircleData.PM25;
    private SensorsCollection sensorsCollection;

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        textTemp = view.findViewById(R.id.textTemp);

        textDust = view.findViewById(R.id.textDust);
        textDust2 = view.findViewById(R.id.textDust10);

        textUpdate = view.findViewById(R.id.textUpdate);

        imgPollSmallCircle = view.findViewById(R.id.img_pollution_small_circle);
        imgPollCircle = view.findViewById(R.id.img_pollution_circle);
        imgCircle = view.findViewById(R.id.imgCircle);

        textInfo = view.findViewById(R.id.text_info);

        final TextView textPmType = view.findViewById(R.id.textPmType);

        mainCircleLayout = view.findViewById(R.id.layout_dust);
        mainCircleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainCircleData == MainCircleData.PM25) {
                    mainCircleData = MainCircleData.PM10;
                    textPmType.setText(getResources().getString(R.string.pm10));
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                }

                else {
                    mainCircleData = MainCircleData.PM25;
                    textPmType.setText(getResources().getString(R.string.pm2_5));
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                }
            }
        });


        final ImageView imageConnection = view.findViewById(R.id.imageConnection);

        imgFrame = view.findViewById(R.id.img_frame);
        toastDrawerAnimation =
                new ToastDrawerAnimation(getContext(), this, imgFrame);
        toastDrawerAnimation.start();


        btnConnect = view.findViewById(R.id.btn_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {

            private ConnectionMode connectionMode = ConnectionMode.WiFiConnection;

            @Override
            public void onClick(View v) {
                if (connectionMode == ConnectionMode.WiFiConnection) {
                    imageConnection.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_bluetooth_white_24dp));

                    toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW_AND_HIDE, "Bluetooth mode");

                    connectionMode = ConnectionMode.BluetoothConnection;
                } else {
                    imageConnection.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_wifi_white_24dp));

                    toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW_AND_HIDE, "WiFi mode");

                    connectionMode = ConnectionMode.WiFiConnection;
                }

            }
        });

        ServerConnectionThread serverConnectionThread = new ServerConnectionThread(this);
        serverConnectionThread.start();


        return view;
    }

    @Override
    public void Update(final SensorsCollection sensorsCollection, final String date) {

        if (connectionError) {

            rotationThread.stopAnimation();
            toastDrawerAnimation.startToast(ToastDrawerAnimation.HIDE);

            connectionError = false;
        }

        this.sensorsCollection = sensorsCollection;

        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textTemp.setText(sensorsCollection.getSensorValue(SensorName.TemperatureSensor));
                if (mainCircleData == MainCircleData.PM25){
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                } else {
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                }

                float dustPercent = (Float.parseFloat(sensorsCollection.getSensorValue(SensorName.DustSensor25)) / 200) * 100;
                imgCircle.setColorFilter(greenToRedColor(dustPercent));

                textUpdate.setText(date);
                float pollutionPercent =
                        (Float.parseFloat(sensorsCollection.getSensorValue(SensorName.AirQSensor)) / 255) * 100;
                float angle = (pollutionPercent * 305) / 100;
                imgPollSmallCircle.setRotation(angle);


                imgPollCircle.setColorFilter(greenToRedColor(pollutionPercent));

            }
        });
    }

    @Override
    public void onConnectionError() {
        if (!connectionError) {

            toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW, "No connection");

            rotationThread = new RotationThread(this);
            rotationThread.start();
            rotationThread.startAnimation(0, 305);

            connectionError = true;
        }
    }

    private int greenToRedColor(float percent) {
        int green = 255;
        int red = 0;
        if (percent <= 50) {
            red += percent * 5.1;
        } else {
            red = 255;
            green -= (percent - 50) * 5.1;
        }

        if (green < 0)
            green = 0;
        if (red > 255)
            red = 255;

        return Color.rgb(red, green, 0);
    }

    @Override
    public void onRotationUpdate(final int percent, final int angle) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                imgPollSmallCircle.setRotation(angle);
                imgPollCircle.setColorFilter(greenToRedColor(percent));
            }
        });
    }

    @Override
    public void onToastShow(final String text) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInfo.setText(text);
            }
        });

    }

    @Override
    public void onToastHide() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInfo.setText("");
            }
        });
    }
}
