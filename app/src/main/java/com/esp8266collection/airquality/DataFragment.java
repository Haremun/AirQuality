package com.esp8266collection.airquality;


import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.esp8266collection.airquality.Enums.SensorName;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment
        implements UpdateCallback, RotationCallback, AnimationCallback {


    private TextView textTemp;
    private TextView textDust;
    private TextView textUpdate;
    private ImageView imgPollSmallCircle;
    private ImageView imgPollCircle;
    private ImageView imgCircle;
    private ImageView imgFrame;
    private RotationThread rotationThread;
    private FrameLayout btnConnect;
    private TextView textInfo;

    boolean temp = false;

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        textTemp = view.findViewById(R.id.textTemp);
        textDust = view.findViewById(R.id.textDust);
        textUpdate = view.findViewById(R.id.textUpdate);
        imgPollSmallCircle = view.findViewById(R.id.img_pollution_small_circle);
        imgPollCircle = view.findViewById(R.id.img_pollution_circle);
        imgCircle = view.findViewById(R.id.imgCircle);
        imgFrame = view.findViewById(R.id.img_frame);
        btnConnect = view.findViewById(R.id.btn_connect);
        textInfo = view.findViewById(R.id.text_info);

        final AnimationCallback animationCallback = this;
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Drawable drawable;
                if (!temp) {
                    drawable = getResources().getDrawable(R.drawable.show_information_frame);
                    temp = true;
                } else {
                    drawable = getResources().getDrawable(R.drawable.hide_information_frame);
                    temp = false;
                }
                imgFrame.setImageDrawable(drawable);
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) imgFrame.getDrawable();
                animatedVectorDrawable.start();
                AnimationTask animationTask = new AnimationTask(animationCallback);
                animationTask.execute(temp);
            }
        });

        ServerConnectionThread serverConnectionThread = new ServerConnectionThread(this);
        serverConnectionThread.start();


        rotationThread = new RotationThread(this);
        rotationThread.start();
        rotationThread.startAnimation(0, 305);

        return view;
    }

    @Override
    public void Update(final SensorsCollection sensorsCollection, final String date) {
        rotationThread.stopAnimation();
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textTemp.setText(sensorsCollection.getSensorValue(SensorName.TemperatureSensor));
                textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor));
                float dustPercent = (Float.parseFloat(sensorsCollection.getSensorValue(SensorName.DustSensor)) / 200) * 100;
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
    public void AnimationUpdate(final int percent, final int angle) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                imgPollSmallCircle.setRotation(angle);
                imgPollCircle.setColorFilter(greenToRedColor(percent));
            }
        });
    }

    @Override
    public void onAnimationEnd() {
        if (temp)
            textInfo.setText("No connection");
        else
            textInfo.setText("");
    }
}
