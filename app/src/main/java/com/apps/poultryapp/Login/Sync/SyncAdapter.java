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

    // Indices para las columnas indicadas en la proyección



    public static final int COLUMNA_ID = 0;
    public static final int COLUMNA_ID_REMOTA = 1;
    public static final int COLUMNA_NAME = 2;


    public static final int COLUMNA_COMPANY = 3;
    public static final int COLUMNA_FINALIZED= 4;
    public static final int COLUMNA_CRATE = 5;
    public static final int COLUMNA_UPDATE= 6;

    public static final int COLUMNA_BATCH_GALPON = 3;
    public static final int COLUMNA_COMPANY_GALPON = 4;

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
            realizarSincronizacionLocalGalpon(syncResult);
        } else {
            realizarSincronizacionRemota();
            realizarSincronizacionRemotaGalpones();
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

            switch (estado) {
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocales(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
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
                case Constantes.SUCCESS: // EXITO
                    actualizarDatosLocalesGalpon(response, syncResult);
                    break;
                case Constantes.FAILED: // FALLIDO
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
                final int idLocal = c.getInt(COLUMNA_ID);
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
            String idRemota = response.getString(Constantes.ID);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacion(idRemota, idLocal);
                    break;

                case Constantes.FAILED:
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
            String idRemota = response.getString(Constantes.ID);

            switch (estado) {
                case Constantes.SUCCESS:
                    Log.i(TAG, mensaje);
                    finalizarActualizacionGalpones(idRemota, idLocal);
                    break;

                case Constantes.FAILED:
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
            expenseMap.put((String.valueOf(e.id)), e);
        }

        // Consultar registros remotos actuales
        Uri uri = ContratosData.CONTENT_URI;
        String select = ContratosData.Batches.ID_REMOTA + " IS NOT NULL";
        Cursor c = resolver.query(uri, PROJECTION, select, null, null);
        assert c != null;

        Log.i(TAG, "Se encontraron " + c.getCount() + " registros locales.");

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

                if ( b1 || b2 || b3 || b4 || b5) {

                    Log.i(TAG, "Programando actualización de: " + existingUri);

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
            Log.i(TAG, "Programando inserción de: " + e.id);
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

        WareHouse[] res = gson.fromJson(warehouse != null ? warehouse.toString() : null, WareHouse[].class);
        List<WareHouse> data = Arrays.asList(res);

        // Lista para recolección de operaciones pendientes
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        // Tabla hash para recibir las entradas entrantes
        HashMap<String, WareHouse> expenseMap = new HashMap<String, WareHouse>();
        for (WareHouse e : data) {
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

            id = c.getString(0);
            name = c.getString(1);
            batch = c.getString(2);
            company = c.getString(3);
            created = c.getString(4);
            update = c.getString(5);

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

                    Log.i(TAG, "Programando actualización de: " + existingUri);

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
