package com.esp8266collection.airquality.Views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.esp8266collection.airquality.Fragments.SettingsFragment;
import com.esp8266collection.airquality.R;

public class BtnSettings implements View.OnClickListener {

    private FrameLayout button;
    private Context context;
    private boolean isClicked = false;

    public BtnSettings(Context context, FrameLayout button) {
        this.button = button;
        this.context = context;
        this.button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(!isClicked){
            SettingsFragment settingsFragment = new SettingsFragment();
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
