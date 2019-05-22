package com.esp8266collection.airquality;


import android.content.Context;
import android.graphics.Color;
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
import com.esp8266collection.airquality.Callbacks.BluetoothCallback;
import com.esp8266collection.airquality.Callbacks.RotationCallback;
import com.esp8266collection.airquality.Callbacks.UpdateCallback;
import com.esp8266collection.airquality.Database.DatabaseFunctions;
import com.esp8266collection.airquality.Database.SQLiteHelper;
import com.esp8266collection.airquality.Enums.ConnectionMode;
import com.esp8266collection.airquality.Enums.MainCircleData;
import com.esp8266collection.airquality.Enums.SensorName;
import com.esp8266collection.airquality.Enums.TemperatureMode;
import com.esp8266collection.airquality.Sensors.SensorsCollection;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment
        implements UpdateCallback, RotationCallback, AnimationCallback, BluetoothCallback {

    //Text views
    private TextView textTemp;
    private TextView textDust;
    private TextView textDust2;
    private TextView textUpdate;
    private TextView textInfo;
    private TextView textTempUnit;
    //Image views
    private ImageView imgPollSmallCircle;
    private ImageView imgPollCircle;
    private ImageView imgCircle;
    private ImageView imgDustSmallCircle;
    private ImageView imgFrame;
    //Layouts
    private FrameLayout btnConnect;
    private ConstraintLayout mainCircleLayout;
    private FrameLayout temperatureLayout;
    //Threads
    private RotationThread rotationThread;
    private ToastDrawerAnimation toastDrawerAnimation;

    private boolean connectionError = false;
    private MainCircleData mainCircleData = MainCircleData.PM25;
    private TemperatureMode temperatureMode = TemperatureMode.Celsius;
    private SensorsCollection sensorsCollection;

    private SQLiteHelper helper;
    private DataChartFragment dataChartFragment;

    private String actualUpdateDate = "";

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_data, container, false);

        //Finding views from layout

        //Text views
        //Sensors
        textTemp = view.findViewById(R.id.textTemp);
        textDust = view.findViewById(R.id.textDust);
        textDust2 = view.findViewById(R.id.textDust10);
        textUpdate = view.findViewById(R.id.textUpdate);
        //Toast drawer
        textInfo = view.findViewById(R.id.text_info);

        //Image views
        imgPollSmallCircle = view.findViewById(R.id.img_pollution_small_circle);
        imgPollCircle = view.findViewById(R.id.img_pollution_circle);
        imgCircle = view.findViewById(R.id.imgCircle);
        imgDustSmallCircle = view.findViewById(R.id.small_dust_circle);

        //Main circle and onClick listener
        final TextView textPmType = view.findViewById(R.id.textPmType); //PM type

        mainCircleLayout = view.findViewById(R.id.layout_dust); //Getting circle layout
        mainCircleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //Adding onClick listener

                float dustPercentMainCircle;
                float dustPercentSmallCircle;

                if (mainCircleData == MainCircleData.PM25) { //Changing for PM10 mode
                    mainCircleData = MainCircleData.PM10;
                    textPmType.setText(getResources().getString(R.string.pm10));
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));

                    dustPercentMainCircle =
                            (sensorsCollection.getSensor(SensorName.DustSensor10).getSensorValue() / 200) * 100;
                    dustPercentSmallCircle =
                            (sensorsCollection.getSensor(SensorName.DustSensor25).getSensorValue() / 200) * 100;
                } else {                                    //Changing for PM25 mode
                    mainCircleData = MainCircleData.PM25;
                    textPmType.setText(getResources().getString(R.string.pm25));
                    textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                    textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));

                    dustPercentMainCircle =
                            (sensorsCollection.getSensor(SensorName.DustSensor25).getSensorValue() / 200) * 100;
                    dustPercentSmallCircle =
                            (sensorsCollection.getSensor(SensorName.DustSensor10).getSensorValue() / 200) * 100;
                }

                imgCircle.setColorFilter(greenToRedColor(dustPercentMainCircle));
                imgDustSmallCircle.setColorFilter(greenToRedColor(dustPercentSmallCircle));
            }
        });

        //Getting connection image view and toast frame
        final ImageView imageConnection = view.findViewById(R.id.imageConnection);

        imgFrame = view.findViewById(R.id.img_frame);

        //Staring toast thread
        toastDrawerAnimation =
                new ToastDrawerAnimation(getContext(), this, imgFrame);
        toastDrawerAnimation.start();

        //OnClick listener for connection type button
        btnConnect = view.findViewById(R.id.btn_connect);
        final Context context = getContext();
        btnConnect.setOnClickListener(new View.OnClickListener() {

            private ConnectionMode connectionMode = ConnectionMode.WiFiConnection;

            @Override
            public void onClick(View v) {
                if (connectionMode == ConnectionMode.WiFiConnection) { //Changing to Bluetooth mode
                    imageConnection.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_bluetooth_white_24dp));

                    v.setClickable(false);
                    toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW_AND_HIDE, "Bluetooth mode");

                    connectionMode = ConnectionMode.BluetoothConnection;

                    BluetoothConnectionThread bluetoothConnectionThread =
                            new BluetoothConnectionThread(context, DataFragment.this);
                    bluetoothConnectionThread.start();

                } else {                                                //Changing to WiFi mode
                    imageConnection.setImageDrawable(
                            getResources().getDrawable(R.drawable.ic_wifi_white_24dp));

                    v.setClickable(false);
                    toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW_AND_HIDE, "WiFi mode");

                    connectionMode = ConnectionMode.WiFiConnection;
                }

            }
        });

        //Setting onClick listener for Temperature Layout
        temperatureLayout = view.findViewById(R.id.layoutTemperature);
        textTempUnit = view.findViewById(R.id.text_temperature_unit);

        temperatureLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temperatureMode == TemperatureMode.Celsius) { //Changing for Fahrenheit mode
                    temperatureMode = TemperatureMode.Fahrenheit;
                    float temp = sensorsCollection.getSensor(SensorName.TemperatureSensor).getSensorValue();
                    int value = (int) ((temp * 1.8) + 32);
                    textTemp.setText(String.valueOf(value));
                    textTempUnit.setText(getResources().getString(R.string.fahrenheit_unit));
                } else {                                        //Changing for Celsius mode
                    temperatureMode = TemperatureMode.Celsius;
                    textTemp.setText(sensorsCollection.getSensorValue(SensorName.TemperatureSensor));
                    textTempUnit.setText(getResources().getString(R.string.celsius_unit));
                }
            }
        });

        //Creating new sqlite helper
        helper = new SQLiteHelper(getContext());

        //Starting connection with server and getting updates
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

        if (!actualUpdateDate.equals(date)) {

            actualUpdateDate = date;

            //Adding new data to local database
            DatabaseFunctions databaseFunctions = new DatabaseFunctions(helper);
            databaseFunctions.addToDatabase((int) sensorsCollection.getSensor(SensorName.DustSensor25).getSensorValue(),
                    (int) sensorsCollection.getSensor(SensorName.DustSensor10).getSensorValue());

            //Update chart
            if (dataChartFragment != null) {
                dataChartFragment.updateChart();
            }

            //Update views with new data
            Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    float dustPercentMainCircle;
                    float dustPercentSmallCircle;

                    if (temperatureMode == TemperatureMode.Celsius) {
                        textTemp.setText(sensorsCollection.getSensorValue(SensorName.TemperatureSensor));
                        textTempUnit.setText(getResources().getString(R.string.celsius_unit));
                    } else {
                        float temp = sensorsCollection.getSensor(SensorName.TemperatureSensor).getSensorValue();
                        int value = (int) ((temp * 1.8) + 32);
                        textTemp.setText(String.valueOf(value));
                        textTempUnit.setText(getResources().getString(R.string.fahrenheit_unit));
                    }

                    if (mainCircleData == MainCircleData.PM25) {
                        textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));
                        textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));

                        dustPercentMainCircle =
                                (sensorsCollection.getSensor(SensorName.DustSensor25).getSensorValue() / 200) * 100;
                        dustPercentSmallCircle =
                                (sensorsCollection.getSensor(SensorName.DustSensor10).getSensorValue() / 200) * 100;
                    } else {
                        textDust.setText(sensorsCollection.getSensorValue(SensorName.DustSensor10));
                        textDust2.setText(sensorsCollection.getSensorValue(SensorName.DustSensor25));

                        dustPercentMainCircle =
                                (sensorsCollection.getSensor(SensorName.DustSensor10).getSensorValue() / 200) * 100;
                        dustPercentSmallCircle =
                                (sensorsCollection.getSensor(SensorName.DustSensor25).getSensorValue() / 200) * 100;
                    }

                    imgCircle.setColorFilter(greenToRedColor(dustPercentMainCircle));
                    imgDustSmallCircle.setColorFilter(greenToRedColor(dustPercentSmallCircle));

                    textUpdate.setText(date);
                    float pollutionPercent =
                            (Float.parseFloat(sensorsCollection.getSensorValue(SensorName.AirQSensor)) / 255) * 100;
                    float angle = (pollutionPercent * 305) / 100;
                    imgPollSmallCircle.setRotation(angle);


                    imgPollCircle.setColorFilter(greenToRedColor(pollutionPercent));

                }
            });
        }


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

    public void setDataChartFragment(DataChartFragment dataChartFragment) {
        this.dataChartFragment = dataChartFragment;
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

    @Override
    public void onToastEnd() {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnConnect.setClickable(true);
            }
        });
    }

    @Override
    public void onBluetoothConnect(BluetoothManagementThread bluetoothManagementThread) {
        toastDrawerAnimation.startToast(ToastDrawerAnimation.SHOW_AND_HIDE, "Bluetooth connected");
    }
}
