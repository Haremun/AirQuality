package com.esp8266collection.airquality.OnClickListeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.esp8266collection.airquality.Bluetooth.BluetoothManagementThread;
import com.esp8266collection.airquality.SettingsMessage;
import com.esp8266collection.airquality.Vibrations;

import java.util.Objects;

import static java.security.AccessController.getContext;

public class AcceptButtonListener implements View.OnClickListener {

    private Context context;
    private SettingsMessage settingsMessage;
    private BluetoothManagementThread bluetoothManagementThread;

    public AcceptButtonListener(Context context, SettingsMessage message) {
        this.context = context;
        this.settingsMessage = message;
    }

    public void setBluetoothManagementThread(BluetoothManagementThread bluetoothManagementThread) {
        this.bluetoothManagementThread = bluetoothManagementThread;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!settingsMessage.getNetworkName().equals("-") && !settingsMessage.getNetworkPassword().equals("-")) {
            sharedPreferences.edit()
                    .putString("Network", settingsMessage.getNetworkName())
                    .putInt("Intervals", settingsMessage.getIntervalIndex())
                    .apply();
            ((AppCompatActivity) context).getSupportFragmentManager().popBackStack();


        }
        if (bluetoothManagementThread != null)
            bluetoothManagementThread.writeMessage(settingsMessage);

    }
}
