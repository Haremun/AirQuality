package com.esp8266collection.airquality;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DataFragment dataFragment = new DataFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_data_container, dataFragment);
        transaction.commit();

        DataChartFragment dataChartFragment = new DataChartFragment();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.fragment_chart_container, dataChartFragment);
        transaction2.commit();

        dataFragment.setDataChartFragment(dataChartFragment);

    }
}
