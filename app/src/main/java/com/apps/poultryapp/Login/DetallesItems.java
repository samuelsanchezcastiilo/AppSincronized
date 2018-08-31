package com.apps.poultryapp.Login;

import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.widget.LinearLayout;

import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Provider.DataLocalHelper;
import com.apps.poultryapp.Login.adapter.AdapterCorrals;
import com.apps.poultryapp.Login.adapter.AdapterPesajes;
import com.apps.poultryapp.Login.adapter.AdapterWarehouse;
import com.apps.poultryapp.R;

public class DetallesItems extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerViewGalpones;
    private RecyclerView recyclerViewPesajes;
    private RecyclerView recyclerViewCorrales;

    private AdapterWarehouse adapterWarehouse;
    private AdapterCorrals adapterCorrals;
    private AdapterPesajes adapterPesajes;

    private LinearLayoutManager layoutManagerGalpon;
    private LinearLayoutManager layoutManagerPesajes;
    private LinearLayoutManager layoutManagerCorrales;

    private LinearLayout layoutGalpones;
    private LinearLayout layoutCorrales;
    private LinearLayout layoutPesajes;
    String lote;
    String wareHouseid;
    String corral;

    private SQLiteDatabase sql ;

    String selection;
    String[] selectionArgsWarehouse ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_items);

        recyclerViewGalpones =  (RecyclerView) findViewById(R.id.detail_galpones);
        recyclerViewPesajes =  (RecyclerView) findViewById(R.id.detail_pesajes);
        recyclerViewCorrales =  (RecyclerView) findViewById(R.id.detail_corrales);

        layoutGalpones = (LinearLayout) findViewById(R.id.recycler_galpones_detalles);
        layoutCorrales = (LinearLayout) findViewById(R.id.recycler_lotes_detalles);
        layoutPesajes = (LinearLayout) findViewById(R.id.recycler_pesajes_detalles);


        layoutPesajes.setVisibility(View.INVISIBLE);
        layoutGalpones.setVisibility(View.INVISIBLE);
        layoutCorrales.setVisibility(View.INVISIBLE);


        DataLocalHelper dataLocalHelper = new DataLocalHelper(getApplicationContext());
         sql = dataLocalHelper.getWritableDatabase();

        adapterWarehouse =  new AdapterWarehouse(getApplicationContext());
        adapterCorrals =    new  AdapterCorrals(getApplicationContext());
        adapterPesajes =    new AdapterPesajes(getApplicationContext());

        layoutManagerGalpon =  new LinearLayoutManager(this);
        layoutManagerPesajes =  new LinearLayoutManager(this);
        layoutManagerCorrales =  new LinearLayoutManager(this);


        //recyclerViewGalpones.setAdapter(adapterWarehouse);

        String lugar = getIntent().getStringExtra("origen");

        if (lugar.equals("1")){
              lote = getIntent().getStringExtra("remota");
            DataGalpones();
        }else if(lugar.equals("2")) {
            wareHouseid = getIntent().getStringExtra("idRemota");
            DataCorral();
        }else {
            corral = getIntent().getStringExtra("idRemotaC");
            DataPesaje();

        }

        //DataCorral();
        //DataPesaje();



    }

    public String[] getIDs(String tble, String parm){


        Cursor c = sql.rawQuery("SELECT idRemota FROM "+tble+"  WHERE idRemota = '"+lote+"' ",null);
        String[] args= new String[c.getCount()];
        try {
            if (c.moveToFirst()){

            args[0] = c.getString(0);


                System.out.println("ene el metodo de consulta");
                lote = c.getString(0);
                System.out.println("-----" + lote);
                System.out.println(c.getString(0));
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return args;
    }
    public void DataCorral(){
        layoutPesajes.setVisibility(View.INVISIBLE);
        layoutGalpones.setVisibility(View.INVISIBLE);
        layoutCorrales.setVisibility(View.VISIBLE);
        recyclerViewCorrales.setLayoutManager(layoutManagerCorrales);
        recyclerViewCorrales.setAdapter(adapterCorrals);
        onCreateLoader(2,null);
        getSupportLoaderManager().initLoader(2,null,this);

    }

    public void DataGalpones(){
        layoutPesajes.setVisibility(View.INVISIBLE);
        layoutCorrales.setVisibility(View.INVISIBLE);
        layoutGalpones.setVisibility(View.VISIBLE);
        recyclerViewGalpones.setLayoutManager(layoutManagerGalpon);
        recyclerViewGalpones.setAdapter(adapterWarehouse);
        onCreateLoader(1,null);
        getSupportLoaderManager().initLoader(1,null,this);

    }

    public void DataPesaje(){
        layoutPesajes.setVisibility(View.VISIBLE);
        layoutCorrales.setVisibility(View.INVISIBLE);
        layoutGalpones.setVisibility(View.INVISIBLE);

        recyclerViewPesajes.setLayoutManager(layoutManagerPesajes);
        recyclerViewPesajes.setAdapter(adapterPesajes);
        onCreateLoader(3,null);
        getSupportLoaderManager().initLoader(3,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String batches = ContratosData.Warehouse.BATCH + " =?";
        String warehouse = ContratosData.Corrals.WAREHOUSE + " =?";
        String pesaje = ContratosData.Weighings.CORRAL + " =?";



        if (id == 1){
            System.out.println("en el id1 " + id);
            String[] selectionArgs ={lote};
            return   new CursorLoader(this,ContratosData.CONTENT_URI_WAREHOUSE,null,batches,selectionArgs,null);

        }else if(id == 2){
            System.out.println("en el id2 " + id);
            String[] selectionArgs ={wareHouseid};
            return new  CursorLoader(this,ContratosData.CONTENT_URI_CORRALS,null,warehouse,selectionArgs,null);
        }else{
            String[] selectionArgs ={corral};
            System.out.println("en el id3 " + id);
            return new CursorLoader(this,ContratosData.CONTENT_URI_WEIGHINGS,null,pesaje,selectionArgs,null);
        }



    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        System.out.println(loader);
        if (loader.getId() == 1){
            adapterWarehouse.swapCursor(data);
        }else if (loader.getId() == 2){
            adapterCorrals.swapCursor(data);
        }else if (loader.getId() == 3){
            adapterPesajes.swapCursor(data);
        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        System.out.println(loader);
        if (loader.getId() == 1){
            adapterWarehouse.swapCursor(null);
        }else if (loader.getId() == 2){
        adapterCorrals.swapCursor(null);
    }else if (loader.getId() == 3){
        adapterPesajes.swapCursor(null);
    }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
