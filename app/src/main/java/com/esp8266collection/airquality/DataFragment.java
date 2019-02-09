package com.esp8266collection.airquality;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataFragment extends Fragment implements UpdateCallback {


    private TextView textTemp;
    private TextView textAirQ;
    private TextView textDust;

    public DataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_data, container, false);

        textTemp = view.findViewById(R.id.textTemp);
        textAirQ = view.findViewById(R.id.textAirQ);
        textDust = view.findViewById(R.id.textDust);

        ServerConnectionThread serverConnectionThread = new ServerConnectionThread(this);
        serverConnectionThread.start();

        return view;
    }

    @Override
    public void Update(final String[] parts) {
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {

                textTemp.setText(parts[0]);
                textAirQ.setText(parts[1]);
                textDust.setText(parts[2]);

            }
        });
    }
}
