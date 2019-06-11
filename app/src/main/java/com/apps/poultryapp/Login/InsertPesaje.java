package com.apps.poultryapp.Login;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowId;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Sync.SyncAdapter;
import com.apps.poultryapp.Login.Utils.Utilidades;
import com.apps.poultryapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InsertPesaje extends AppCompatActivity  {

    LinearLayout linearLayoutAddBrids;
    private ImageButton newBrid;
    private Button saveData;
    public int contadorBrids = 2;
    private int sum = 0;
    EditText[] pesajeAves;
    EditText pesajP;
    EditText age;
    EditText standars;
    EditText pesaje;
    List<String> pesajes;
    String bridsData = "1:";
    String corral ;
    private static final String TAG = "InsertPesaje";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_pesaje);
        linearLayoutAddBrids =  (LinearLayout) findViewById(R.id.add_new_ave);
        newBrid = (ImageButton) findViewById(R.id.newBrid);
        saveData = (Button) findViewById(R.id.save_data_pesajes);
        pesajP = (EditText) findViewById(R.id.value_pesaje);
        age = (EditText) findViewById(R.id.text_age_corral);
        standars = (EditText) findViewById(R.id.peso_estandar);
        pesajes = new ArrayList<String>();
        pesajeAves  = new EditText[100];
        corral = getIntent().getStringExtra("corral");
        newBrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newBrids();
            }
        });


        pesajP.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //aqui iria tu codigo al presionar el boton enter o done
                    newBrids();
                    pesaje.requestFocus();

                }
              return  false;
            }
        });





        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pesajP.getText().toString().isEmpty() || standars.getText().toString().isEmpty()){
                    pesajP.setError("Escriba el pesaje");
                    standars.setError("Completar campos");
                    return;
                }
                if (age.getText().toString().isEmpty()){
                    age.setError("Escriba el pesaje");
                    return;
                }

                bridsData = bridsData+pesajP.getText().toString();
                for (int i = 0; i < pesajeAves.length ; i++) {
                    if (i < sum){
                        bridsData = bridsData + " | " + String.valueOf(i+2) + ":" + pesajeAves[i].getText().toString();
                    }
                }
                String company =   SessionPref.get(getApplication()).getPrefUserCompany();
                String edad = age.getText().toString();
                ContentValues contentValues =  new ContentValues();
                contentValues.put(ContratosData.Weighings.NAME,"pesaje "+edad);
                contentValues.put(ContratosData.Weighings.BRIDS,bridsData);
                contentValues.put(ContratosData.Weighings.AGE,edad);
                contentValues.put(ContratosData.Weighings.COMPANY,company);
                contentValues.put(ContratosData.Weighings.CORRAL,corral);
                contentValues.put(ContratosData.Weighings.STANDDAR_WEIGHT,standars.getText().toString());
                contentValues.put(ContratosData.Weighings.CREATE,getDateTime());
                contentValues.put(ContratosData.Weighings.UPDATE,getDateTime());
                contentValues.put(ContratosData.Weighings.PENDIENTE_INSERCION, 1);
                getApplication().getContentResolver().insert(ContratosData.CONTENT_URI_WEIGHINGS,contentValues);
                SyncAdapter.sincronizarAhora(getApplication(),true);
                if (Utilidades.materialDesign())
                    finishAfterTransition();
                else finish();


            }
        });

    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void newBrids(){
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.new_brid,null,false);

        TextView contBrids = (TextView) view.findViewById(R.id.code_pesaje_ave);
       pesaje = (EditText) view.findViewById(R.id.value_pesaje);
        contBrids.setText(String.valueOf(contadorBrids));

        pesaje.setId(contadorBrids);
        pesajeAves[sum] = pesaje;
        sum++;
        pesaje.setHint("Peso del Ave: " + String.valueOf(contadorBrids));
        pesaje.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    //aqui iria tu codigo al presionar el boton enter o done
                    newBrids();
                    pesaje.requestFocus();
                    return true;
                }
                return false;
            }
        });


        contadorBrids++;
        linearLayoutAddBrids.addView(view);
    }



}
