package com.apps.poultryapp.Login.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.apps.poultryapp.Login.Login.Data.SessionPref;
import com.apps.poultryapp.Login.Model.Batches;

import com.apps.poultryapp.Login.Model.Corrals;
import com.apps.poultryapp.Login.Model.WareHouse;
import com.apps.poultryapp.Login.Provider.ContratosData;
import com.apps.poultryapp.Login.Utils.Constantes;
import com.apps.poultryapp.Login.Utils.Utilidades;
import com.apps.poultryapp.Login.Web.VolleySingleton;
import com.apps.poultryapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private static final String TAG = SyncAdapter.class.getSimpleName();

    ContentResolver resolver;
    private Gson gson = new Gson();

    /**
     * Proyección para las consultas
     */
    private static final String[] PROJECTION = new String[]{

            ContratosData.Batches.ID,
            ContratosData.Batches.ID_REMOTA,
            ContratosData.Batches.NAME,
            ContratosData.Batches.COMPANY,
            ContratosData.Batches.FINALIZED,
            ContratosData.Batches.CREATE,
            ContratosData.Batches.UPDATE
    };
    private static final String[] PROJECTION_GALPON = new String[]{

            ContratosData.Warehouse.ID,
            ContratosData.Warehouse.ID_REMOTA,
            ContratosData.Warehouse.NAME,
            ContratosData.Warehouse.BATCH,
            ContratosData.Warehouse.COMPANY,
            ContratosData.Warehouse.CREATE,
            ContratosData.Warehouse.UPDATE
    };

    private static final String[] PROJECTION_CORRAL = new String[]{

            ContratosData.Corrals.ID,
            ContratosData.Corrals.ID_REMOTA,
            ContratosData.Corrals.NAME,
            ContratosData.Corrals.WAREHOUSE,
            ContratosData.Corrals.AGE,
            ContratosData.Corrals.COMPANY,
            ContratosData.Corrals.CREATE,
            ContratosData.Corrals.UPDATE
    };

    // Indices para las columnas indicadas en la proyección



    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_NAME = 2;
    public static final int COLUMNA_COMPANY = 3;
    public static final int COLUMNA_FINALIZED= 4;
    public static final int COLUMNA_CRATE = 5;
    public static final int COLUMNA_UPDATE= 6;




    public static final int COLUMNA_ID_GALPON = 0;
    public static final int COLUMNA_ID_REMOTA_GALPON = 1;
    public static final int COLUMNA_NAME_GALPON = 2;
    public static final int COLUMNA_BATCH_GALPON = 3;
    public static final int COLUMNA_COMPANY_GALPON= 4;
    public static final int COLUMNA_CRATE_GALPON = 5;
    public static final int COLUMNA_UPDATE_GALPON= 6;


    public static final int COLUMNA_ID_CORRALS = 0;
    public static final int COLUMNA_ID_REMOTA_CORRALS = 1;
    public static final int COLUMNA_NAME_GALPON_CORRALS = 2;
    public static final int COLUMNA_WAREHOUSE_CORRALS = 3;
    public static final int COLUMNA_AGE_CORRALS= 4;
    public static final int COLUMNA_COMPANY_CORRALS= 5;
    public static final int COLUMNA_CRATE_CORRALS = 6;
    public static final int COLUMNA_UPDATE_CORRALS= 7;






    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        resolver = context.getContentResolver();
    }

    /**
     * Constructor para mantener compatibilidad en versiones inferiores a 3.0
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        resolver = context.getContentResolver();
    }

    public static void inicializarSyncAdapter(Context context) {
        obtenerCuentaASincronizar(context);
    }

    @Override
    public void onPerformSync(Account account,
                              Bundle extras,
                              String authority,
                              ContentProviderClient provider,
                              final SyncResult syncResult) {
        Log.i(TAG, "onPerformSync()...");
        boolean soloSubida = extras.getBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, false);

        if (!soloSubida) {
           realizarSincronizacionLocal(syncResult);
            //realizarSincronizacionLocalGalpon(syncResult);
            //realizarSincronizacionLocalCorrals(syncResult);
        } else {
           realizarSincronizacionRemota();
            //realizarSincronizacionRemotaGalpones();
            //realizarSincronizacionRemotaCorrals();

        }
    }

    private void realizarSincronizacionLocal(final SyncResult syncResult) {

        Log.i(TAG, "Actualizando el cliente.");
        HashMap<String, String> params = new HashMap<String, String>();
        String company =  SessionPref.get(getContext()).getPrefUserCompany();
        params.put("company", company);

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_URL,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGet(response, syncResult);
                                Log.e(TAG, "onResponse: de lotes " +response );
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d(TAG, error.networkResponse.toString());
                                Toast.makeText(getContext(),"sincronixacion Faliida",Toast.LENGTH_SHORT).show();
                            }
                        }
                )
        );
    }

    private void realizarSincronizacionLocalGalpon(final SyncResult syncResult) {

        Log.i(TAG, "Actualizando el cliente en galpon.");
        HashMap<String, String> params = new HashMap<String, String>();
        String company =  SessionPref.get(getContext()).getPrefUserCompany();
        params.put("company", company);

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_URL_WAREHOUSE,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetGalpon(response, syncResult);
                                Log.e(TAG, "onResponse de galpones: " +response );
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
        Log.e(TAG, "realizarSincronizacionLocalGalpon: saliendo" );
    }

    private void realizarSincronizacionLocalCorrals(final SyncResult syncResult) {

        Log.i(TAG, "Actualizando el cliente en galpon.");
        HashMap<String, String> params = new HashMap<String, String>();
        String company =  SessionPref.get(getContext()).getPrefUserCompany();
        params.put("company", company);

        VolleySingleton.getInstance(getContext()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.GET_URL_CORRALS,
                        new JSONObject(params),
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                procesarRespuestaGetCorrals(response, syncResult);
                                Log.e(TAG, "onResponse de corrals: " +response );
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //Log.d(TAG, error.networkResponse.toString());
                            }
                        }
                )
        );
        Log.e(TAG, "realizarSincronizacionLocalGalpon: saliendo" );
    }

    /**
     * Procesa la respuesta del servidor al pedir que se retornen todos los gastos.
     *
     * @param response   Respuesta en formato Json
     * @param syncResult Registro de resultados de sincronización
     */
    private void procesarRespuestaGet(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);
            Log.e(TAG, "procesarRespuestaGet: " +estado );

            switch (estado) {
                case Constantes.SUCCESS_BATCHES: // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case Constantes.FAILED_BATCHES: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void procesarRespuestaGetGalpon(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS_WAREHOUSE: // EXITO
                    actualizarDatosLocalesGalpon(response, syncResult);
                    break;
                case Constantes.FAILED_WAREHOUSE: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void procesarRespuestaGetCorrals(JSONObject response, SyncResult syncResult) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString(Constantes.ESTADO);

            switch (estado) {
                case Constantes.SUCCESS_WAREHOUSE: // EXITO
                    actualizarDatosLocalesCorrals(response, syncResult);
                    break;
                case Constantes.FAILED_WAREHOUSE: // FALLIDO
                    String mensaje = response.getString(Constantes.MENSAJE);
                    Log.i(TAG, mensaje);
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void realizarSincronizacionRemota() {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacion();

        Cursor c = obtenerRegistrosSucios();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_URL,
                                Utilidades.deCursorAJSONObject(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsert(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley: " + error.getMessage());
                                    }
                                }
                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }

    private void realizarSincronizacionRemotaGalpones() {
        Log.i(TAG, "Actualizando el servidor...");

        iniciarActualizacionGalpones();

        Cursor c = obtenerRegistrosSuciosGalpones();

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios en galpones.");

        if (c.getCount() > 0) {
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID_GALPON);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_URL_WAREHOUSE,
                                Utilidades.deCursorAJSONObjectGalpones(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertGalpones(response, idLocal);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley de galpones: " + error.getMessage());
                                    }
                                }

                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }
        c.close();
    }
    private void realizarSincronizacionRemotaCorrals() {
        Log.i(TAG, "Actualizando el servidor...");
        iniciarActualizacionCorrals();
        Cursor c = obtenerRegistrosSuciosCorrals();
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros sucios en corrals.");

        if (c.getCount() > 0) {
            Log.e(TAG, "realizarSincronizacionRemotaCorrals: antes del while" );
            while (c.moveToNext()) {
                final int idLocal = c.getInt(COLUMNA_ID_CORRALS);
                VolleySingleton.getInstance(getContext()).addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.POST,
                                Constantes.INSERT_URL_CORRALS,
                                Utilidades.deCursorAJSONObjectCorrals(c),
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        procesarRespuestaInsertCorrals(response, idLocal);
                                        Log.e(TAG, "onResponse: de corrals------------------------");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.d(TAG, "Error Volley de corrals: " + error.getMessage());
                                    }
                                }

                        ) {
                            @Override
                            public Map<String, String> getHeaders() {
                                Map<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json; charset=utf-8");
                                headers.put("Accept", "application/json");
                                return headers;
                            }

                            @Override
                            public String getBodyContentType() {
                                return "application/json; charset=utf-8" + getParamsEncoding();
                            }
                        }
                );
            }

        } else {
            Log.i(TAG, "No se requiere sincronización en corralssss++++++++++++++");
        }
        c.close();
    }

    /**
     * Obtiene el registro que se acaba de marcar como "pendiente por sincronizar" y
     * con "estado de sincronización"
     *
     * @return Cursor con el registro.
     */
    private Cursor obtenerRegistrosSucios() {
        Uri uri = ContratosData.CONTENT_URI;
        String selection = ContratosData.Batches.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Batches.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION, selection, selectionArgs, null);
    }

    private Cursor obtenerRegistrosSuciosGalpones() {
        Uri uri = ContratosData.CONTENT_URI_WAREHOUSE;
        String selection = ContratosData.Warehouse.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Warehouse.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION_GALPON, selection, selectionArgs, null);
    }

    private Cursor obtenerRegistrosSuciosCorrals() {

        Uri uri = ContratosData.CONTENT_URI_CORRALS;
        String selection = ContratosData.Corrals.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Corrals.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_SYNC + ""};

        return resolver.query(uri, PROJECTION_CORRAL, selection, selectionArgs, null);
    }

    /**
     * Cambia a estado "de sincronización" el registro que se acaba de insertar localmente
     */
    private void iniciarActualizacion() {
        Uri uri = ContratosData.CONTENT_URI;
        String selection = ContratosData.Batches.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Batches.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Batches.ESTADO, ContratosData.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }


    private void iniciarActualizacionGalpones() {
        Uri uri = ContratosData.CONTENT_URI_WAREHOUSE;
        String selection = ContratosData.Warehouse.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Warehouse.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Warehouse.ESTADO, ContratosData.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción:" + results);
    }

    private void iniciarActualizacionCorrals() {
        Uri uri = ContratosData.CONTENT_URI_CORRALS;
        String selection = ContratosData.Corrals.PENDIENTE_INSERCION + "=? AND "
                + ContratosData.Corrals.ESTADO + "=?";
        String[] selectionArgs = new String[]{"1", ContratosData.ESTADO_OK + ""};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Corrals.ESTADO, ContratosData.ESTADO_SYNC);

        int results = resolver.update(uri, v, selection, selectionArgs);
        Log.i(TAG, "Registros puestos en cola de inserción en corrals:" + results);
    }

    /**
     * Limpia el registro que se sincronizó y le asigna la nueva id remota proveida
     * por el servidor
     *
     * @param idRemota id remota
     */
    private void finalizarActualizacion(String idRemota, int idLocal) {
        Uri uri = ContratosData.CONTENT_URI;
        String selection = ContratosData.Batches.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Batches.PENDIENTE_INSERCION, "0");
        v.put(ContratosData.Batches.ESTADO, ContratosData.ESTADO_OK);
        v.put(ContratosData.Batches.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

    private void finalizarActualizacionGalpones(String idRemota, int idLocal) {
        Uri uri = ContratosData.CONTENT_URI_WAREHOUSE;
        String selection = ContratosData.Warehouse.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Warehouse.PENDIENTE_INSERCION, "0");
        v.put(ContratosData.Warehouse.ESTADO, ContratosData.ESTADO_OK);
        v.put(ContratosData.Warehouse.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }

    private void finalizarActualizacionCorrals(String idRemota, int idLocal) {
        Uri uri = ContratosData.CONTENT_URI_CORRALS;
        String selection = ContratosData.Corrals.ID + "=?";
        String[] selectionArgs = new String[]{String.valueOf(idLocal)};

        ContentValues v = new ContentValues();
        v.put(ContratosData.Corrals.PENDIENTE_INSERCION, "0");
        v.put(ContratosData.Corrals.ESTADO, ContratosData.ESTADO_OK);
        v.put(ContratosData.Corrals.ID_REMOTA, idRemota);

        resolver.update(uri, v, selection, selectionArgs);
    }


    /**
     * Procesa los diferentes tipos de respuesta obtenidos del servidor
     *
     * @param response Respuesta en formato Json
     */
    public void procesarRespuestaInsert(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(Constantes.ID_BATCH);

            switch (estado) {
                case Constantes.SUCCESS_BATCHES:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(idRemota, idLocal);
                    break;
                case Constantes.FAILED_BATCHES:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void procesarRespuestaInsertGalpones(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(Constantes.ID_WAREHOUSE);

            switch (estado) {
                case Constantes.SUCCESS_WAREHOUSE:
                    Log.i(TAG, mensaje);
                    finalizarActualizacionGalpones(idRemota, idLocal);
                    break;

                case Constantes.FAILED_WAREHOUSE:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void procesarRespuestaInsertCorrals(JSONObject response, int idLocal) {

        try {
            // Obtener estado
            String estado = response.getString(Constantes.ESTADO);
            // Obtener mensaje
            String mensaje = response.getString(Constantes.MENSAJE);
            // Obtener identificador del nuevo registro creado en el servidor
            String idRemota = response.getString(Constantes.ID_WAREHOUSE);

            switch (estado) {
                case Constantes.SUCCESS_WAREHOUSE:
                    Log.i(TAG, mensaje);
                    finalizarActualizacionCorrals(idRemota, idLocal);
                    break;

                case Constantes.FAILED_WAREHOUSE:
                    Log.i(TAG, mensaje);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Actualiza los registros locales a través de una comparación con los datos
     * del servidor
     *
     * @param response   Respuesta en formato Json obtenida del servidor
     * @param syncResult Registros de la sincronización
     */
    private void actualizarDatosLocales(JSONObject response, SyncResult syncResult) {

        JSONArray batches = null;

        try {
            // Obtener array "gastos"
            batches = response.getJSONArray(Constantes.BATCHES);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson
        Batches[] res = gson.fromJson(batches != null ? batches.toString() : null, Batches[].class);
        List<Batches> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, Batches> expenseMap = new HashMap<String, Batches>();
        for (Batches e : data) {
            //expenseMap.put((String.valueOf(e.id)), e);
            expenseMap.put(e.id, e);

        }

        // Consultar registros remotos actuales
        Uri uri = ContratosData.CONTENT_URI;
        String select = ContratosData.Batches.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;
        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales en lotes .");

        // Encontrar datos obsoletos
        String id;
        String name;
        String company;
        String finalized;
        String created;
        String update;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;
            id = c.getString(COLUMNA_ID_REMOTA);
            name = c.getString(COLUMNA_NAME);
            company = c.getString(COLUMNA_COMPANY);
            finalized = c.getString(COLUMNA_FINALIZED);
            created = c.getString(COLUMNA_CRATE);
            update = c.getString(COLUMNA_UPDATE);

            Batches match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);
                Uri existingUri = ContratosData.CONTENT_URI.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado

                boolean b1 = match.name != null && !match.name.equals(name);
                boolean b2 = match.company != null && !match.company.equals(company);
                boolean b3 = match.finalized != null && !match.finalized.equals(finalized);
                boolean b4 = match.created_at != null && !match.created_at.equals(created);
                boolean b5 = match.updated_at != null && !match.updated_at.equals(update);

                if (b1 || b2 || b3 || b4 || b5) {

                    Log.i(TAG, "Programando actualización de lotes: " + existingUri);

                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContratosData.Batches.NAME, match.name)
                            .withValue(ContratosData.Batches.COMPANY, match.company)
                            .withValue(ContratosData.Batches.FINALIZED, match.finalized)
                            .withValue(ContratosData.Batches.CREATE, match.created_at)
                            .withValue(ContratosData.Batches.UPDATE, match.updated_at)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContratosData.CONTENT_URI.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Batches e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: lotes " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContratosData.CONTENT_URI)
                    .withValue(ContratosData.Batches.ID_REMOTA, e.id)
                    .withValue(ContratosData.Batches.NAME, e.name)
                    .withValue(ContratosData.Batches.COMPANY, e.company)
                    .withValue(ContratosData.Batches.FINALIZED, e.finalized)
                    .withValue(ContratosData.Batches.CREATE, e.created_at)
                    .withValue(ContratosData.Batches.UPDATE, e.updated_at)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContratosData.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                e.printStackTrace();
            }
            resolver.notifyChange(
                    ContratosData.CONTENT_URI,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    private void actualizarDatosLocalesGalpon(JSONObject response, SyncResult syncResult) {

        JSONArray warehouse = null;

        try {
            // Obtener array "gastos"
            warehouse = response.getJSONArray(Constantes.WAREHOUSE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson

        WareHouse[] ga = gson.fromJson(warehouse != null ? warehouse.toString() : null, WareHouse[].class);
        List<WareHouse> dataga = Arrays.asList(ga);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, WareHouse> expenseMap = new HashMap<String, WareHouse>();
        for (WareHouse e : dataga) {
            expenseMap.put((String.valueOf(e.id)), e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContratosData.CONTENT_URI_WAREHOUSE;
        String select = ContratosData.Warehouse.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION_GALPON, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

        // Encontrar datos obsoletos
        String id;
        String name;
        String company;
        String batch;
        String created;
        String update;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA_GALPON);
            name = c.getString(COLUMNA_NAME_GALPON);
            batch = c.getString(COLUMNA_BATCH_GALPON);
            company = c.getString(COLUMNA_COMPANY_GALPON);
            created = c.getString(COLUMNA_CRATE_GALPON);
            update = c.getString(COLUMNA_UPDATE_GALPON);

            WareHouse match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContratosData.CONTENT_URI_WAREHOUSE.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado

                boolean b1 = match.name != null && !match.name.equals(name);
                boolean b2 = match.company != null && !match.company.equals(company);
                boolean b3 = match.batch != null && !match.batch.equals(batch);
                boolean b4 = match.created_at != null && !match.created_at.equals(created);
                boolean b5 = match.updated_at != null && !match.updated_at.equals(update);

                if ( b1 || b2 || b3 || b4 || b5) {
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContratosData.Warehouse.NAME, match.name)
                            .withValue(ContratosData.Warehouse.BATCH, match.batch)
                            .withValue(ContratosData.Warehouse.COMPANY, match.company)
                            .withValue(ContratosData.Warehouse.CREATE, match.created_at)
                            .withValue(ContratosData.Warehouse.UPDATE, match.updated_at)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContratosData.CONTENT_URI_WAREHOUSE.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (WareHouse e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContratosData.CONTENT_URI_WAREHOUSE)
                    .withValue(ContratosData.Warehouse.ID_REMOTA, e.id)
                    .withValue(ContratosData.Warehouse.NAME, e.name)
                    .withValue(ContratosData.Warehouse.BATCH, e.batch)
                    .withValue(ContratosData.Warehouse.COMPANY, e.company)
                    .withValue(ContratosData.Warehouse.CREATE, e.created_at)
                    .withValue(ContratosData.Warehouse.UPDATE, e.updated_at)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContratosData.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(TAG, "actualizarDatosLocalesGalpon: en el catch" );
                e.printStackTrace();

            }
            resolver.notifyChange(
                    ContratosData.CONTENT_URI_WAREHOUSE,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }
    private void actualizarDatosLocalesCorrals(JSONObject response, SyncResult syncResult) {

        JSONArray corrals = null;

        try {
            // Obtener array "gastos"
            corrals = response.getJSONArray(Constantes.CORRALS);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Parsear con Gson

        Corrals[] res = gson.fromJson(corrals != null ? corrals.toString() : null, Corrals[].class);
        List<Corrals> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, Corrals> expenseMap = new HashMap<String, Corrals>();
        for (Corrals e : data) {
            expenseMap.put((String.valueOf(e.id)), e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContratosData.CONTENT_URI_CORRALS;
        String select = ContratosData.Corrals.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION_CORRAL, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales en corrarls.");

        // Encontrar datos obsoletos
        String id;
        String name;
        String warehouse;
        String age;
        String company;
        String created;
        String update;
        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getString(COLUMNA_ID_REMOTA_CORRALS);
            name = c.getString(COLUMNA_NAME_GALPON_CORRALS);
            warehouse = c.getString(COLUMNA_WAREHOUSE_CORRALS);
            age = c.getString(COLUMNA_AGE_CORRALS);
            company = c.getString(COLUMNA_COMPANY_CORRALS);
            created = c.getString(COLUMNA_CRATE_CORRALS);
            update = c.getString(COLUMNA_UPDATE_CORRALS);

            Corrals match = expenseMap.get(id);

            if (match != null) {
                // Esta entrada existe, por lo que se remueve del mapeado
                expenseMap.remove(id);

                Uri existingUri = ContratosData.CONTENT_URI_CORRALS.buildUpon()
                        .appendPath(id).build();

                // Comprobar si el gasto necesita ser actualizado

                boolean b1 = match.name != null && !match.name.equals(name);
                boolean b2 = match.warehouse != null && !match.warehouse.equals(warehouse);
                boolean b3 = match.age != null && !match.age.equals(age);
                boolean b4 = match.company != null && !match.company.equals(company);
                boolean b5 = match.created_at != null && !match.created_at.equals(created);
                boolean b6 = match.updated_at != null && !match.updated_at.equals(update);

                if ( b1 || b2 || b3 || b4 || b5 || b6) {
                    ops.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(ContratosData.Corrals.NAME, match.name)
                            .withValue(ContratosData.Corrals.WAREHOUSE, match.warehouse)
                            .withValue(ContratosData.Corrals.AGE, match.age)
                            .withValue(ContratosData.Corrals.COMPANY, match.company)
                            .withValue(ContratosData.Corrals.CREATE, match.created_at)
                            .withValue(ContratosData.Corrals.UPDATE, match.updated_at)
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No hay acciones para este registro: " + existingUri);
                }
            } else {
                // Debido a que la entrada no existe, es removida de la base de datos
                Uri deleteUri = ContratosData.CONTENT_URI_WAREHOUSE.buildUpon()
                        .appendPath(id).build();
                Log.i(TAG, "Programando eliminación de: " + deleteUri);
                ops.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();

        // Insertar items resultantes
        for (Corrals e : expenseMap.values()) {
            Log.i(TAG, "Programando inserción de: " + e.id);
            ops.add(ContentProviderOperation.newInsert(ContratosData.CONTENT_URI_CORRALS)
                    .withValue(ContratosData.Corrals.ID_REMOTA, e.id)
                    .withValue(ContratosData.Corrals.NAME, e.name)
                    .withValue(ContratosData.Corrals.WAREHOUSE, e.warehouse)
                    .withValue(ContratosData.Corrals.AGE, e.age)
                    .withValue(ContratosData.Corrals.COMPANY, e.company)
                    .withValue(ContratosData.Corrals.CREATE, e.created_at)
                    .withValue(ContratosData.Corrals.UPDATE, e.updated_at)
                    .build());
            syncResult.stats.numInserts++;
        }

        if (syncResult.stats.numInserts > 0 ||
                syncResult.stats.numUpdates > 0 ||
                syncResult.stats.numDeletes > 0) {
            Log.i(TAG, "Aplicando operaciones...");
            try {
                resolver.applyBatch(ContratosData.AUTHORITY, ops);
            } catch (RemoteException | OperationApplicationException e) {
                Log.e(TAG, "actualizarDatosLocalesGalpon: en el catch" );
                e.printStackTrace();

            }
            resolver.notifyChange(
                    ContratosData.CONTENT_URI_CORRALS,
                    null,
                    false);
            Log.i(TAG, "Sincronización finalizada.");

        } else {
            Log.i(TAG, "No se requiere sincronización");
        }

    }

    /**
     * Inicia manualmente la sincronización
     *
     * @param context    Contexto para crear la petición de sincronización
     * @param onlyUpload Usa true para sincronizar el servidor o false para sincronizar el cliente
     */
    public static void sincronizarAhora(Context context, boolean onlyUpload) {
        Log.i(TAG, "Realizando petición de sincronización manual.");
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        if (onlyUpload)
            bundle.putBoolean(ContentResolver.SYNC_EXTRAS_UPLOAD, true);
        ContentResolver.requestSync(obtenerCuentaASincronizar(context),
                context.getString(R.string.provider_authority), bundle);
    }

    /**
     * Crea u obtiene una cuenta existente
     *
     * @param context Contexto para acceder al administrador de cuentas
     * @return cuenta auxiliar.
     */
    public static Account obtenerCuentaASincronizar(Context context) {
        // Obtener instancia del administrador de cuentas
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        // Crear cuenta por defecto
        Account newAccount = new Account(
                context.getString(R.string.app_name), Constantes.ACCOUNT_TYPE);

        // Comprobar existencia de la cuenta
        if (null == accountManager.getPassword(newAccount)) {

            // Añadir la cuenta al account manager sin password y sin datos de usuario
            if (!accountManager.addAccountExplicitly(newAccount, "", null))
                return null;

        }
        Log.i(TAG, "Cuenta de usuario obtenida.");
        return newAccount;
    }
}
