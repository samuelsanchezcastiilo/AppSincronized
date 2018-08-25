package com.apps.poultryapp.Login.Provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import static com.android.volley.VolleyLog.TAG;

public class ProviderData  extends ContentProvider {

    public static final String URI_NO_SOPORTADA = "Uri no soportada";

    private ContentResolver resolver;
    /**
     * Instancia del administrador de BD
     */
    private  DataLocalHelper dataLocalHelper;


    // [URI_MATCHER]
    public static final UriMatcher uriMatcher;

    // Casos

    public static final int BATCHES= 300;
    public static final int BATCHES_ROW= 301;



    public static final int CORRALS = 400;
    public static final int CORRALS_ROW = 401;

    public static final int WAREHOUSE = 500;
    public static final int WAREHOUSE_ROW = 501;
    public static final int WEIGHINGS = 600;
    public static final int WEIGHINGS_ROW = 601;



  /*  public static final int DETALLES_PEDIDOS = 200;
    public static final int DETALLES_PEDIDOS_ID = 201;

    public static final int PRODUCTOS = 300;
    public static final int PRODUCTOS_ID = 301;

    public static final int CLIENTES = 400;
    public static final int CLIENTES_ID = 401;

    public static final int FORMAS_PAGO = 500;
    public static final int FORMAS_PAGO_ID = 501;*/

    public static final String AUTORIDAD = "com.apps.poultryapp";



    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

               /* obtener todos los datos en general */
        uriMatcher.addURI(AUTORIDAD,"batches",BATCHES);
        uriMatcher.addURI(AUTORIDAD,"warehouse",WAREHOUSE);
        uriMatcher.addURI(AUTORIDAD,"corrals",CORRALS);
        uriMatcher.addURI(AUTORIDAD,"weighings",WEIGHINGS);

        /* LOS DATOS POR ID*/
        uriMatcher.addURI(AUTORIDAD,"batches/*",BATCHES_ROW);
        uriMatcher.addURI(AUTORIDAD,"warehouse/*",WAREHOUSE_ROW);
        uriMatcher.addURI(AUTORIDAD,"corrals/*",CORRALS_ROW);
        uriMatcher.addURI(AUTORIDAD,"weighings/*",WEIGHINGS_ROW);
    }


    @Override
    public boolean onCreate() {
        dataLocalHelper = new DataLocalHelper(getContext());
        resolver = getContext().getContentResolver();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = dataLocalHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        String id;
        Cursor c;
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();


        switch (match) {
            case BATCHES:
                // Consultando todos los registros
                c = db.query(DataLocalHelper.Tablas.BATCHES, projection,
                        selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case BATCHES_ROW:
                id = ContratosData.Batches.obtnerIdBatches(uri);
                c = db.query(DataLocalHelper.Tablas.BATCHES, projection,
                        ContratosData.ColumnasBatches.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                break;
            case WAREHOUSE:
                Log.e(TAG, "query: warehose" );
                c = db.query(DataLocalHelper.Tablas.WAREHOUSE,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WAREHOUSE_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                c = db.query(DataLocalHelper.Tablas.WAREHOUSE, projection,
                        ContratosData.ColumnasWarehouse.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                break;
            case CORRALS:
                c =db.query(DataLocalHelper.Tablas.CORRALS,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case CORRALS_ROW:
                id= ContratosData.Corrals.obtnerIdCorrals(uri);
                c = db.query(DataLocalHelper.Tablas.CORRALS, projection,
                        ContratosData.ColumnasCorrals.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                break;
            case WEIGHINGS:
                c =db.query(DataLocalHelper.Tablas.WEIGHINGS,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case WEIGHINGS_ROW:
                id= ContratosData.Weighings.obtnerIdWeighings(uri);
                c = db.query(DataLocalHelper.Tablas.WEIGHINGS, projection,
                        ContratosData.ColumnasWeighings.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
    c.setNotificationUri(resolver,uri);
    return c;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)){
            case BATCHES:
                return ContratosData.generarMime("batches");
            case BATCHES_ROW:
                return  ContratosData.generarMimeItem("batches");
            case WAREHOUSE:
                return ContratosData.generarMime("warehouse");
            case WAREHOUSE_ROW:
                return ContratosData.generarMimeItem("warehouse");
            case CORRALS:
                return  ContratosData.generarMimeItem("corrals");
            case CORRALS_ROW:
                return  ContratosData.generarMimeItem("corrals");
            case WEIGHINGS:
                return  ContratosData.generarMimeItem("weighings");
            case WEIGHINGS_ROW:
                return  ContratosData.generarMimeItem("weighings");
                default:
                    throw new IllegalArgumentException("Tipo de Uri desconocida: " + uri);
        }

        /*switch (ContratosData.uriMatcher.match(uri)) {
            case ContratosData.ALLROWS:
                return ContratosData.MULTIPLE_MIME;
            case ContratosData.SINGLE_ROW:
                return ContratosData.SINGLE_MIME;
            default:
                throw new IllegalArgumentException("Tipo de gasto desconocido: " + uri);*/

    }
    private void notificarCambio(Uri uri) {
        resolver.notifyChange(uri, null,false);
    }
    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Log.d(TAG, "Inserción en " + uri + "( " + values.toString() + " )\n");
        SQLiteDatabase db = dataLocalHelper.getWritableDatabase();

        /*ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }*/

        switch (uriMatcher.match(uri)){
            case BATCHES:
                db.insert(DataLocalHelper.Tablas.BATCHES,null,values);
                notificarCambio(uri);
                return ContratosData.Batches.craerUriBatches(values.getAsString(DataLocalHelper.Tablas.BATCHES));
            case WAREHOUSE:
                db.insert(DataLocalHelper.Tablas.WAREHOUSE,null,values);
                notificarCambio(uri);
                return ContratosData.Warehouse.craerUriWarehouse(values.getAsString(DataLocalHelper.Tablas.WAREHOUSE));
            case CORRALS:
                db.insert(DataLocalHelper.Tablas.CORRALS,null,values);
                notificarCambio(uri);
                return ContratosData.Corrals.craerUriCorrals(values.getAsString(DataLocalHelper.Tablas.CORRALS));
            case  WEIGHINGS:
                db.insert(DataLocalHelper.Tablas.WEIGHINGS,null,values);
                notificarCambio(uri);
                return ContratosData.Weighings.craerUriWeighings(values.getAsString(DataLocalHelper.Tablas.WEIGHINGS));


            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);

        }



        // Inserción de nueva fila
       /* long rowId = db.insert(ContratosData.BATCHES, null, contentValues);
        if (rowId > 0) {
            Uri uri_lote = ContentUris.withAppendedId(
                    ContratosData.CONTENT_URI, rowId);
            resolver.notifyChange(uri_lote, null, false);
            return uri_lote;
        }
        Log.e(TAG, "insert: " );
        throw new SQLException("Falla al insertar fila en : " + uri);*/
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dataLocalHelper.getWritableDatabase();
        String id ;
        int affected;
        switch (uriMatcher.match(uri)){
            case BATCHES:
                affected = db.delete(DataLocalHelper.Tablas.BATCHES,
                        selection,
                        selectionArgs);
                break;
            case BATCHES_ROW:
                id = ContratosData.Batches.obtnerIdBatches(uri);
                affected = db.delete(DataLocalHelper.Tablas.BATCHES,
                        ContratosData.Batches.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case WAREHOUSE:
                affected = db.delete(DataLocalHelper.Tablas.WAREHOUSE,
                        selection,
                        selectionArgs);
                break;
            case WAREHOUSE_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                affected = db.delete(DataLocalHelper.Tablas.WAREHOUSE,
                        ContratosData.Warehouse.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case CORRALS:
                affected = db.delete(DataLocalHelper.Tablas.CORRALS,selection,selectionArgs);
                break;
            case CORRALS_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                affected =  db.delete(DataLocalHelper.Tablas.CORRALS,
                        ContratosData.Corrals.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case WEIGHINGS:
                affected = db.delete(DataLocalHelper.Tablas.WEIGHINGS,selection,selectionArgs);
                break;
            case WEIGHINGS_ROW:
                id = ContratosData.Weighings.obtnerIdWeighings(uri);
                affected =  db.delete(DataLocalHelper.Tablas.WEIGHINGS,
                        ContratosData.Weighings.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);

        }

        /*switch (match) {
            case ContratosData.ALLROWS:
                affected = db.delete(ContratosData.BATCHES,
                        selection,
                        selectionArgs);
                break;
            case ContratosData.SINGLE_ROW:
                long idGasto = ContentUris.parseId(uri);
                affected = db.delete(ContratosData.BATCHES,
                        ContratosData.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                // Notificar cambio asociado a la uri
                resolver.
                        notifyChange(uri, null, false);
                break;
            default:
                throw new IllegalArgumentException("Elemento Lote desconocido: " +
                        uri);
        }*/
        return affected;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dataLocalHelper.getWritableDatabase();
        String id;
        int affected;
        switch (uriMatcher.match(uri)){
            case BATCHES:
                affected = db.update(DataLocalHelper.Tablas.BATCHES, values,
                        selection, selectionArgs);
                break;
            case BATCHES_ROW:
                id = ContratosData.Batches.obtnerIdBatches(uri);
                affected = db.update(DataLocalHelper.Tablas.BATCHES, values,
                        ContratosData.Batches.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case WAREHOUSE:
                affected = db.update(DataLocalHelper.Tablas.WAREHOUSE, values,
                        selection, selectionArgs);
                notificarCambio(uri);
                break;
            case WAREHOUSE_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                affected = db.update(DataLocalHelper.Tablas.WAREHOUSE, values,
                        ContratosData.Warehouse.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case CORRALS:
                affected = db.update(DataLocalHelper.Tablas.WAREHOUSE,values,selection,selectionArgs);
                notificarCambio(uri);
                break;
            case CORRALS_ROW:
                id = ContratosData.Corrals.obtnerIdCorrals(uri);
                affected = db.update(DataLocalHelper.Tablas.CORRALS, values,
                        ContratosData.Corrals.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;
            case WEIGHINGS:
                affected = db.update(DataLocalHelper.Tablas.WEIGHINGS,values,selection,selectionArgs);
                notificarCambio(uri);
                break;
            case WEIGHINGS_ROW:
                id = ContratosData.Weighings.obtnerIdWeighings(uri);
                affected = db.update(DataLocalHelper.Tablas.WEIGHINGS, values,
                        ContratosData.Weighings.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                notificarCambio(uri);
                break;


            default:
                Log.e(TAG, "update: "+ uri );
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }

        /*switch (ContratosData.uriMatcher.match(uri)) {
            case ContratosData.ALLROWS:
                affected = db.update(ContratosData.BATCHES, values,
                        selection, selectionArgs);
                break;
            case ContratosData.SINGLE_ROW:
                String idGasto = uri.getPathSegments().get(1);
                affected = db.update(ContratosData.BATCHES, values,
                        ContratosData.Columnas.ID_REMOTA + "=" + idGasto
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("URI desconocida: " + uri);
        }
        resolver.notifyChange(uri, null, false);*/
        return affected;

    }
}
