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

public class NewGalpon extends DialogFragment{

    public NewGalpon() {
    }
    View view;
    EditText galpon;
    Spinner lote;
    ArrayAdapter<String> adapterLotes;
    List<String> lLotes;
    String nameLot;
    String idLote;
    DataLocalHelper dataLocalHelper;
    SQLiteDatabase sql;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return NewGalpon();
    }

    public AlertDialog NewGalpon(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.new_register_galpon,null);

        galpon = (EditText) view.findViewById(R.id.text_value_galpon);
        lote = (Spinner)view.findViewById(R.id.code_lotes);
        lLotes = new ArrayList<>();
        dataLocalHelper  = new DataLocalHelper(getContext());
        sql = dataLocalHelper.getWritableDatabase();
        adapterLotes = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,lLotes);
        lote.setAdapter(adapterLotes);
        String prueba = idLoteSelect();
        System.out.println("prueba" +prueba);
        getLotes();



        Button saveGalpon = (Button) view.findViewById(R.id.new_galpon);

        saveGalpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGalpon();

            }
        });

        alertDialog.setView(view);
        return alertDialog.create();
    }
    public void getLotes(){
        Cursor c = sql.rawQuery("SELECT name FROM batches",null);
        try {
            if (c.moveToFirst()){
                do {
                    lLotes.add(c.getString(0));
                    adapterLotes.notifyDataSetChanged();
                }while (c.moveToNext());
            }

        }catch (Exception e )
        {
            System.out.println(e);
        }

    }
    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String idLoteSelect(){

        lote.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nameLot = parent.getItemAtPosition(position).toString();
                Cursor c = sql.rawQuery("SELECT idRemota FROM batches WHERE name = '"+nameLot+"' ",null);
                try {
                    if (c.moveToFirst()){
                        idLote = c.getString(0);
                    }
                }catch (Exception e){
                    System.out.println(e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        System.out.println("valor del id"+idLote);
        return idLote;
    }

    public void saveGalpon(){
        if (galpon.getText() == null || galpon.getText().equals("")){
            galpon.setError("escriba un nombre ");
            return;
        }
        String galponw = galpon.getText().toString();
        String company =   SessionPref.get(getContext()).getPrefUserCompany();


        ContentValues contentValues = new ContentValues();
        contentValues.put(ContratosData.Warehouse.NAME,galponw);
        contentValues.put(ContratosData.Warehouse.BATCH,idLoteSelect());
        contentValues.put(ContratosData.Warehouse.COMPANY,company);
        contentValues.put(ContratosData.Warehouse.CREATE,getDateTime());
        contentValues.put(ContratosData.Warehouse.UPDATE,getDateTime());

        getActivity().getContentResolver().insert(ContratosData.CONTENT_URI_WAREHOUSE,contentValues);
        SyncAdapter.sincronizarAhora(getContext(),true);
        dismiss();

    }

}

