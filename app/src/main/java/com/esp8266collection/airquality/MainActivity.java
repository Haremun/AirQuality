package com.esp8266collection.airquality;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esp8266collection.airquality.Fragments.DataChartFragment;
import com.esp8266collection.airquality.Fragments.DataFragment;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataFragment dataFragment = new DataFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_data_container, dataFragment)
                .commit();

        DataChartFragment dataChartFragment = new DataChartFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_chart_container, dataChartFragment)
                .commit();

        dataFragment.setDataChartFragment(dataChartFragment);

    }
}
