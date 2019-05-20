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

        SQLiteHelper helper = new SQLiteHelper(getContext()); //żeby wycieku nie było
        LoadFromDatabaseTask task = new LoadFromDatabaseTask(helper, this);
        task.execute();

        Legend legend = chart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);

        /*List<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 5));
        entries.add(new Entry(2, 5));
        entries.add(new Entry(3, 6));
        entries.add(new Entry(4, 5));
        entries.add(new Entry(5, 7));
        entries.add(new Entry(6, 7));
        entries.add(new Entry(7, 10));
        entries.add(new Entry(8, 10));
        entries.add(new Entry(9, 8));
        entries.add(new Entry(10, 7));
        entries.add(new Entry(11, 7));
        entries.add(new Entry(12, 5));

        List<Entry> entries2 = new ArrayList<>();
        entries2.add(new Entry(1, 10));
        entries2.add(new Entry(2, 11));
        entries2.add(new Entry(3, 12));
        entries2.add(new Entry(4, 14));
        entries2.add(new Entry(5, 12));
        entries2.add(new Entry(6, 12));
        entries2.add(new Entry(7, 12));
        entries2.add(new Entry(8, 12));
        entries2.add(new Entry(9, 13));
        entries2.add(new Entry(10, 10));
        entries2.add(new Entry(11, 9));
        entries2.add(new Entry(12, 9));*/

        /*LineDataSet dataSet = new LineDataSet(entries, "PM 2,5"); // add entries to dataset
        dataSet.setColor(Color.GREEN);
        dataSet.setValueTextColor(Color.RED); // styling, ...


        LineDataSet dataSet2 = new LineDataSet(entries2, "PM 10"); // add entries to dataset
        dataSet2.setColor(Color.RED);
        dataSet2.setValueTextColor(Color.BLUE); // styling, ...


        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        dataSets.add(dataSet2);

        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);

        chart.invalidate(); // refresh*/
        return view;
    }

    @Override
    public void onDatabaseLoad(List<DustValues> dustValuesList) {

        if (dustValuesList != null) {
            List<Entry> entriesPm25 = new ArrayList<>();
            List<Entry> entriesPm10 = new ArrayList<>();
            int i = 1;
            for (DustValues values :
                    dustValuesList) {
                entriesPm10.add(new Entry(i, values.getPm10()));
                entriesPm25.add(new Entry(i, values.getPm25()));
                i++;
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
