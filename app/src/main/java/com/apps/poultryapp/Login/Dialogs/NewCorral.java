package com.apps.poultryapp.Login.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.apps.poultryapp.R;

public class NewCorral extends DialogFragment{

    public NewCorral() {
    }
    View view;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return newCorral();
    }

    public AlertDialog newCorral(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_register_corral,null);

        Button saveCorrar = (Button) view.findViewById(R.id.new_corral);

        alertDialog.setView(view);
        return alertDialog.create();
    }
}
