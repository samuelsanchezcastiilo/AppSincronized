package com.apps.poultryapp.Login.Utils;

import android.database.Cursor;
import android.os.Build;
import android.util.Log;

import com.apps.poultryapp.Login.Provider.ContratosData;

import org.json.JSONException;
import org.json.JSONObject;

public class Utilidades {

    // Indices para las columnas indicadas en la proyección




    public static final int COLUMNA_NAME = 2;
    public static final int COLUMNA_COMPANY = 3;
    public static final int COLUMNA_FINALIZED= 4;
    public static final int COLUMNA_CRATE = 5;
    public static final int COLUMNA_UPDATE= 6;

    public static final int COLUMNA_NAME_GALPON = 2;
    public static final int COLUMNA_BATCH_GALPON= 3;
    public static final int COLUMNA_COMPANY_GALPON = 4;
    public static final int COLUMNA_CRATE_GALPON = 5;
    public static final int COLUMNA_UPDATE_GALPON= 6;


    public static final int COLUMNA_NAME_CORRAL = 2;
    public static final int COLUMNA_WAREHOUSE_CORRAL= 3;
    public static final int COLUMNA_AGE_CORRAL = 4;
    public static final int COLUMNA_COMPANY_CORRAL = 5;
    public static final int COLUMNA_CRATE_CORRAL= 6;
    public static final int COLUMNA_UPDATE_CORRAL= 7;



    public static final int COLUMNA_NAME_WEIGHINGS= 2;
    public static final int COLUMNA_BRIDS_WEIGHINGS= 3;
    public static final int COLUMNA_AGE_WEIGHINGS = 4;
    public static final int COLUMNA_COMPANY_WEIGHINGS = 5;
    public static final int COLUMNA_CORRAL_WEIGHINGS = 6;
    public static final int COLUMNA_STANDAR_WEIGHINGS = 7;
    public static final int COLUMNA_CRATE_WEIGHINGS= 8;
    public static final int COLUMNA_UPDATE_WEIGHINGS= 9;



    /**
     * Determina si la aplicación corre en versiones superiores o iguales
     * a Android LOLLIPOP
     *
     * @return booleano de confirmación
     */
    public static boolean materialDesign() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * Copia los datos de un gasto almacenados en un cursor hacia un
     * JSONObject
     *
     * @param c cursor
     * @return objeto jason
     */
    public static JSONObject deCursorAJSONObject(Cursor c) {
        JSONObject jObject = new JSONObject();
        String name;
        String company;
        String finalized;
        String create;
        String update;

        name = c.getString(COLUMNA_NAME);
        company = c.getString(COLUMNA_COMPANY);
        finalized = c.getString(COLUMNA_FINALIZED);
        create = c.getString(COLUMNA_CRATE);
        update = c.getString(COLUMNA_UPDATE);

        try {
            jObject.put(ContratosData.Batches.NAME, name);
            jObject.put(ContratosData.Batches.COMPANY, company);
            jObject.put(ContratosData.Batches.FINALIZED, finalized);
            jObject.put(ContratosData.Batches.CREATE, create);
            jObject.put(ContratosData.Batches.UPDATE, update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }


    public static JSONObject deCursorAJSONObjectGalpones(Cursor c) {
        JSONObject jObject = new JSONObject();
        String name;
        String batch;
        String company;
        String create;
        String update;

        name = c.getString(COLUMNA_NAME_GALPON);
        batch = c.getString(COLUMNA_BATCH_GALPON);
        company = c.getString(COLUMNA_COMPANY_GALPON);
        create = c.getString(COLUMNA_CRATE_GALPON);
        update = c.getString(COLUMNA_UPDATE_GALPON);

        try {
            jObject.put(ContratosData.Warehouse.NAME, name);
            jObject.put(ContratosData.Warehouse.COMPANY, company);
            jObject.put(ContratosData.Warehouse.BATCH, batch);
            jObject.put(ContratosData.Warehouse.CREATE, create);
            jObject.put(ContratosData.Warehouse.UPDATE, update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }



    public static JSONObject deCursorAJSONObjectCorrals(Cursor c) {
        JSONObject jObject = new JSONObject();
        String name;
        String warehouse;
        String age;
        String company;
        String create;
        String update;

        name = c.getString(COLUMNA_NAME_CORRAL);
        warehouse = c.getString(COLUMNA_WAREHOUSE_CORRAL);
        age =  c.getString(COLUMNA_AGE_CORRAL);
        company = c.getString(COLUMNA_COMPANY_CORRAL);
        create = c.getString(COLUMNA_CRATE_CORRAL);
        update = c.getString(COLUMNA_UPDATE_CORRAL);

        try {
            jObject.put(ContratosData.Corrals.NAME, name);
            jObject.put(ContratosData.Corrals.WAREHOUSE, warehouse);
            jObject.put(ContratosData.Corrals.AGE, age);
            jObject.put(ContratosData.Corrals.COMPANY, company);
            jObject.put(ContratosData.Corrals.CREATE, create);
            jObject.put(ContratosData.Corrals.UPDATE, update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }

    public static JSONObject deCursorAJSONObjectWeighings(Cursor c) {
        JSONObject jObject = new JSONObject();
        String name;
        String brids;
        String age;
        String company;
        String corral;
        String standard_weight;
        String create;
        String update;

        name = c.getString(COLUMNA_NAME_WEIGHINGS);
        brids = c.getString(COLUMNA_BRIDS_WEIGHINGS);
        age =  c.getString(COLUMNA_AGE_WEIGHINGS);
        company = c.getString(COLUMNA_COMPANY_WEIGHINGS);
        corral   = c.getString(COLUMNA_CORRAL_WEIGHINGS);
        standard_weight = c.getString(COLUMNA_STANDAR_WEIGHINGS);
        create = c.getString(COLUMNA_CRATE_WEIGHINGS);
        update = c.getString(COLUMNA_UPDATE_WEIGHINGS);

        try {
            jObject.put(ContratosData.Weighings.NAME, name);
            jObject.put(ContratosData.Weighings.BRIDS, brids);
            jObject.put(ContratosData.Weighings.AGE, age);
            jObject.put(ContratosData.Weighings.COMPANY, company);
            jObject.put(ContratosData.Weighings.CORRAL, corral);
            jObject.put(ContratosData.Weighings.STANDDAR_WEIGHT,standard_weight);
            jObject.put(ContratosData.Weighings.CREATE, create);
            jObject.put(ContratosData.Weighings.UPDATE, update);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jObject;
    }


}
