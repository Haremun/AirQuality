package com.esp8266collection.airquality.OnClickListeners;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class BackButtonListener implements View.OnClickListener {

    private Context context;

    public BackButtonListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        ((AppCompatActivity)context).getSupportFragmentManager().popBackStack();
    }
}
