package com.esp8266collection.airquality;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esp8266collection.airquality.Callbacks.DatabaseCallback;
import com.esp8266collection.airquality.Database.LoadFromDatabaseTask;
import com.esp8266collection.airquality.Database.SQLiteHelper;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataChartFragment extends Fragment implements DatabaseCallback {

    private LineChart chart;

    public DataChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_chart, container, false);

        chart = view.findViewById(R.id.chart);

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        //Starting load task when view is created
        updateChart();

        return view;
    }

    public void updateChart(){
        SQLiteHelper helper = new SQLiteHelper(getContext());
        LoadFromDatabaseTask task = new LoadFromDatabaseTask(helper, this);
        task.execute();
    }

    @Override
    public void onDatabaseLoad(List<DustValues> dustValuesList) {

        if (dustValuesList != null) {
            List<Entry> entriesPm25 = new ArrayList<>();
            List<Entry> entriesPm10 = new ArrayList<>();

            for (int i = 1; i < dustValuesList.size(); i++) {
                DustValues values = dustValuesList.get(dustValuesList.size() - i); //reverse data
                entriesPm10.add(new Entry(i, values.getPm10()));
                entriesPm25.add(new Entry(i, values.getPm25()));
            }

            LineDataSet dataSetPm25 = new LineDataSet(entriesPm25, "PM 2,5"); // add entries to dataset
            dataSetPm25.setColor(Color.GREEN);
            dataSetPm25.setValueTextColor(Color.RED);

            LineDataSet dataSetPm10 = new LineDataSet(entriesPm10, "PM 10"); // add entries to dataset
            dataSetPm10.setColor(Color.RED);
            dataSetPm10.setValueTextColor(Color.BLUE);

            List<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(dataSetPm25);
            dataSets.add(dataSetPm10);

            LineData lineData = new LineData(dataSets);

            chart.setData(lineData);
            chart.invalidate(); // refresh
        }
    }
}
