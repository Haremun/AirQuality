package com.esp8266collection.airquality.Views;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.esp8266collection.airquality.R;
import com.esp8266collection.airquality.SettingsMessage;

public class SpinnerGui implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinner;
    private Context mContext;
    private SettingsMessage settingsMessage;

    public SpinnerGui(Context context, Spinner spinner) {
        this.mSpinner = spinner;
        this.mContext = context;
        mSpinner.setOnItemSelectedListener(this);

    }

    public void setSpinnerItem(int index) {
        mSpinner.setSelection(index);
    }

    public void setSettingsMessage(SettingsMessage settingsMessage) {
        this.settingsMessage = settingsMessage;
    }

    public void setArray(int array) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                mContext, array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        settingsMessage.setIntervalIndex(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
