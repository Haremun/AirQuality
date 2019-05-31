package com.esp8266collection.airquality.OnClickListeners;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.esp8266collection.airquality.SettingsMessage;

import java.util.Objects;

import static java.security.AccessController.getContext;

public class AcceptButtonListener implements View.OnClickListener {

    private Context context;
    private SettingsMessage settingsMessage;

    public AcceptButtonListener(Context context, SettingsMessage message) {
        this.context = context;
        this.settingsMessage = message;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        if (!settingsMessage.getNetworkName().isEmpty() && !settingsMessage.getNetworkPassword().isEmpty()){
            sharedPreferences.edit()
                    .putString("Network", settingsMessage.getNetworkName())
                    .putInt("Intervals", settingsMessage.getIntervalIndex())
                    .apply();
            ((AppCompatActivity)context).getSupportFragmentManager().popBackStack();
        } else {
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (vibrator != null && audioManager != null && audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(80, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrator.vibrate(80);
                }
            }
        }

    }
}
