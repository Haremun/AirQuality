package com.esp8266collection.airquality.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.esp8266collection.airquality.R;

import java.util.Objects;

import androidx.annotation.NonNull;

public class SetNetworkDialog extends DialogFragment {

    @android.support.annotation.NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_set_network, null);
        final EditText networkName = view.findViewById(R.id.edit_network_name);
        final EditText password = view.findViewById(R.id.edit_password);
        final DialogFragment dialogFragment = this;

        builder.setView(view)
                .setTitle(getResources().getString(R.string.title_set_network))
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment targetFragment = dialogFragment.getTargetFragment();
                        if (targetFragment != null)
                            targetFragment.onActivityResult(
                                    targetFragment.getTargetRequestCode(), Activity.RESULT_OK, null);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Fragment targetFragment = dialogFragment.getTargetFragment();
                        if (targetFragment != null)
                            targetFragment.onActivityResult(
                                    targetFragment.getTargetRequestCode(), Activity.RESULT_CANCELED, null);
                    }
                });

        return builder.create();
    }
}
