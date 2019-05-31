package com.esp8266collection.airquality.OnClickListeners;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.esp8266collection.airquality.Dialogs.SetNetworkDialog;

public class ChooseNetworkOnClickListener implements View.OnClickListener {

    private Fragment targetFragment;

    public ChooseNetworkOnClickListener(Fragment fragment) {
        this.targetFragment = fragment;
    }

    @Override
    public void onClick(View v) {
        DialogFragment dialogFragment = new SetNetworkDialog();
        dialogFragment.setTargetFragment(targetFragment, 1);
        FragmentManager fragmentManager = targetFragment.getFragmentManager();
        if (fragmentManager != null)
            dialogFragment.show(fragmentManager, "Set up network");
    }
}
