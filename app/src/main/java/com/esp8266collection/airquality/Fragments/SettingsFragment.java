package com.esp8266collection.airquality.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.esp8266collection.airquality.OnClickListeners.BackButtonListener;
import com.esp8266collection.airquality.OnClickListeners.ChooseNetworkOnClickListener;
import com.esp8266collection.airquality.R;
import com.esp8266collection.airquality.Views.BtnSettings;
import com.esp8266collection.airquality.Views.SpinnerGui;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    private BtnSettings btnSettings;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Spinner spinner = view.findViewById(R.id.spinner_time_interval);
        SpinnerGui spinnerGui = new SpinnerGui(getContext(), spinner);
        spinnerGui.setArray(R.array.time_intervals);

        FrameLayout btnBack = view.findViewById(R.id.layout_btn_back);
        btnBack.setOnClickListener(new BackButtonListener(getContext()));

        TextView textChooseNetwork = view.findViewById(R.id.text_choose_network);
        textChooseNetwork.setOnClickListener(new ChooseNetworkOnClickListener(this));
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
}
