package com.apps.poultryapp.Login.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.Login.Utils.Utilidades;
import com.apps.poultryapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewLote extends DialogFragment {

    public NewLote() {
    }
    EditText lote ;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return newLote();
    }
    View view;
    public AlertDialog newLote(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_register_lote,null);
         lote = (EditText) view.findViewById(R.id.text_value_lote);
        Button save = (Button) view.findViewById(R.id.new_lote);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            SaveLote();

            }
        });


        alertDialog.setView(view);
        return alertDialog.create();
    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void SaveLote(){
        if (lote.getText().toString().isEmpty()){
            lote.setError("Escriba un lote");
            return;
        }
        String lot = lote.getText().toString();
      String company =   SessionPref.get(getContext()).getPrefUserCompany();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratosData.Batches.NAME,lot);
        contentValues.put(ContratosData.Batches.COMPANY,company);
        contentValues.put(ContratosData.Batches.FINALIZED,"0");
        contentValues.put(ContratosData.Batches.CREATE,getDateTime());
        contentValues.put(ContratosData.Batches.UPDATE,getDateTime());
        contentValues.put(ContratosData.Batches.PENDIENTE_INSERCION, 1);
        getActivity().getContentResolver().insert(ContratosData.CONTENT_URI,contentValues);
        SyncAdapter.sincronizarAhora(getContext(),true);
        dismiss();
        /*if(Utilidades.materialDesign())getActivity().finishAfterTransition();
        else getActivity().finish();*/

    }
}
