package com.esp8266collection.airquality;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;

public class Vibrations {

    public static void vibrate(Context context){
        mVibrate(context, 80);
    }
    public static void vibrate(Context context, int time){
        mVibrate(context, time);
    }
    private static void mVibrate(Context context, int time){
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (vibrator != null && audioManager != null && audioManager.getRingerMode() != AudioManager.RINGER_MODE_SILENT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrator.vibrate(time);
            }
        }
    }
}
