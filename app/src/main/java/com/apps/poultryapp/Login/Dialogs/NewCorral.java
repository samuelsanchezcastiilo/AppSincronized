package com.apps.poultryapp.Login.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Provider.DataLocalHelper;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewCorral extends DialogFragment{

    public NewCorral() {
    }
    View view;
    EditText nameCorrals;
    Spinner galpones;
    List<String> listGalpones;
    ArrayAdapter<String> adapterGalpones;
    String nameGalpon;
    DataLocalHelper dataLocalHelper;
    SQLiteDatabase sql;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return newCorral();
    }

    public AlertDialog newCorral(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.new_register_corral,null);
        galpones = (Spinner) view.findViewById(R.id.spinner_code_galpon);
        nameCorrals = (EditText) view.findViewById(R.id.text_value_corral);
        listGalpones = new ArrayList<>();

        dataLocalHelper  = new DataLocalHelper(getContext());
        sql = dataLocalHelper.getWritableDatabase();
        nameGalpon = getNameGalpon();

        adapterGalpones = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,listGalpones);
        galpones.setAdapter(adapterGalpones);
        getNameGalpones();

        Button saveCorral = (Button) view.findViewById(R.id.new_corral);
        saveCorral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        saveCorralData();
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
    public void getNameGalpones(){
        Cursor c = sql.rawQuery("SELECT name FROM warehouse",null);
        try {
            if (c.moveToFirst()){
                do {
                    listGalpones.add(c.getString(0));
                    adapterGalpones.notifyDataSetChanged();
                }while (c.moveToNext());
            }

        }catch (Exception e )
        {
            System.out.println(e);
        }

    }
    public String getNameGalpon(){

        galpones.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 nameGalpon = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return nameGalpon;
    }
    public void saveCorralData(){
        if (nameCorrals.getText().toString().isEmpty()){
            nameCorrals.setError("escriba un nombre ");
            return;
        }
        if (listGalpones.size() <= 0){
            Toast.makeText(getContext(),"no tienes galpones",Toast.LENGTH_SHORT).show();
            return;
        }
        String corrals = nameCorrals.getText().toString();
        if (nameGalpon == null)
        {
            nameGalpon = getNameGalpon();
        }
        String company =   SessionPref.get(getContext()).getPrefUserCompany();


        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratosData.Corrals.NAME,corrals);
        contentValues.put(ContratosData.Corrals.WAREHOUSE,nameGalpon);
        contentValues.put(ContratosData.Corrals.COMPANY,company);
        contentValues.put(ContratosData.Corrals.AGE,"0");
        contentValues.put(ContratosData.Corrals.CREATE,getDateTime());
        contentValues.put(ContratosData.Corrals.UPDATE,getDateTime());
        contentValues.put(ContratosData.Corrals.PENDIENTE_INSERCION,1);
        getActivity().getContentResolver().insert(ContratosData.CONTENT_URI_CORRALS,contentValues);
        SyncAdapter.sincronizarAhora(getContext(),true);

        dismiss();

    }
}
