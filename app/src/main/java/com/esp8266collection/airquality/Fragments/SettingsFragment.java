package com.esp8266collection.airquality.Fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.esp8266collection.airquality.OnClickListeners.AcceptButtonListener;
import com.esp8266collection.airquality.OnClickListeners.BackButtonListener;
import com.esp8266collection.airquality.OnClickListeners.ChooseNetworkOnClickListener;
import com.esp8266collection.airquality.R;
import com.esp8266collection.airquality.SettingsMessage;
import com.esp8266collection.airquality.Views.BtnSettings;
import com.esp8266collection.airquality.Views.SpinnerGui;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private BtnSettings btnSettings;
    private String networkName;
    private String networkPassword;
    private TextView textChooseNetwork;
    private SettingsMessage settingsMessage;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        settingsMessage = new SettingsMessage();

        Spinner spinner = view.findViewById(R.id.spinner_time_interval);
        SpinnerGui spinnerGui = new SpinnerGui(getContext(), spinner);
        spinnerGui.setArray(R.array.time_intervals);
        spinnerGui.setSettingsMessage(settingsMessage);

        FrameLayout btnBack = view.findViewById(R.id.layout_btn_back);
        btnBack.setOnClickListener(new BackButtonListener(getContext()));

        FrameLayout btnAccept = view.findViewById(R.id.layout_btn_accept);
        btnAccept.setOnClickListener(
                new AcceptButtonListener(getContext(), settingsMessage));

        textChooseNetwork = view.findViewById(R.id.text_choose_network);
        textChooseNetwork.setOnClickListener(new ChooseNetworkOnClickListener(this));

        if (getContext() != null) {
            SharedPreferences sharedPreferences = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            String name = sharedPreferences.getString("Network", "");
            int index = sharedPreferences.getInt("Intervals", 0);

            if (name != null){
                textChooseNetwork.setText(name);
                spinnerGui.setSpinnerItem(index);
            }


        }


        return view;
    }

    public void setBtnSettings(BtnSettings btnSettings) {
        this.btnSettings = btnSettings;
    }


    @Override
    public void onPause() {
        btnSettings.setClicked(false);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            networkName = data.getStringExtra("Network");
            networkPassword = data.getStringExtra("Password");

            textChooseNetwork.setText(networkName);
            textChooseNetwork.setTextColor(Color.BLACK);

            settingsMessage.setNetworkName(networkName);
            settingsMessage.setNetworkPassword(networkPassword);
        }

    }
}
