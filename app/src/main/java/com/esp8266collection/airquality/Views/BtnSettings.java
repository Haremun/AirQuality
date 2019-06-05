package com.esp8266collection.airquality.Views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.esp8266collection.airquality.Bluetooth.BluetoothManagementThread;
import com.esp8266collection.airquality.Fragments.DataFragment;
import com.esp8266collection.airquality.Fragments.SettingsFragment;
import com.esp8266collection.airquality.R;

public class BtnSettings implements View.OnClickListener {

    //private FrameLayout button;
    private Context context;
    private boolean isClicked = false;
    private SettingsFragment settingsFragment;

    public BtnSettings(Context context, FrameLayout button) {
        //this.button = button;
        this.context = context;

        settingsFragment = new SettingsFragment();

        button.setOnClickListener(this);
    }

    public SettingsFragment getSettingsFragment() {
        return settingsFragment;
    }

    @Override
    public void onClick(View v) {
        if (!isClicked) {
            settingsFragment.setBtnSettings(this);
            ((AppCompatActivity) context).getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_chart_container, settingsFragment)
                    .addToBackStack("")
                    .commit();
            isClicked = true;
        }

    }

    public void setClicked(boolean clicked) {
        isClicked = clicked;
    }
}
