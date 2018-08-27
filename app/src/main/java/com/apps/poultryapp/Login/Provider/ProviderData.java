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

    public static final int BATCHES= 100;
    public static final int BATCHES_ROW= 101;



    public static final int CORRALS = 400;
    public static final int CORRALS_ROW = 401;

    public static final int WAREHOUSE = 500;
    public static final int WAREHOUSE_ROW = 501;
    public static final int WEIGHINGS = 600;
    public static final int WEIGHINGS_ROW = 601;

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
        Cursor cursor;
        switch (match) {
            case BATCHES:
                cursor = db.query(DataLocalHelper.Tablas.BATCHES, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case BATCHES_ROW:
                id = ContratosData.Batches.obtnerIdBatches(uri);
                cursor = db.query(DataLocalHelper.Tablas.BATCHES, projection,
                        ContratosData.ColumnasBatches.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                Log.e(TAG, "query: buscando1= ++++++++++++++++++++++++++++++++++++++" );
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }



       /* switch (match){

            case BATCHES:
                c = db.query(DataLocalHelper.Tablas.BATCHES,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI);
                break;
            case BATCHES_ROW:
                long idBacthes = ContentUris.parseId(uri);

                c = db.query(DataLocalHelper.Tablas.BATCHES, projection, ContratosData.Batches.ID + " = "+idBacthes,
                        selectionArgs, null, null , sortOrder);

                c.setNotificationUri(
                        resolver,
                        ContratosData.CONTENT_URI);
                break;
            case WAREHOUSE:
                c = db.query(DataLocalHelper.Tablas.WAREHOUSE,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_WAREHOUSE);
                break;
            case WAREHOUSE_ROW:
                long idwarehouse = ContentUris.parseId(uri);

                c = db.query(DataLocalHelper.Tablas.WAREHOUSE, projection, ContratosData.Warehouse.ID + " = "+idwarehouse,
                        selectionArgs, null, null , sortOrder);

                c.setNotificationUri(
                        resolver,
                        ContratosData.CONTENT_URI_WAREHOUSE);
                break;
            /*case CORRALS:
                c = db.query(DataLocalHelper.Tablas.CORRALS,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_CORRALS);
                break;
            case CORRALS_ROW:
                long idCorrals = ContentUris.parseId(uri);

                c = db.query(DataLocalHelper.Tablas.CORRALS, projection, ContratosData.Corrals.ID + " = "+idCorrals,
                        selectionArgs, null, null , sortOrder);

                c.setNotificationUri(
                        resolver,
                        ContratosData.CONTENT_URI_CORRALS);
                break;*/
          /*  default:
                throw new IllegalArgumentException("URI no soportada: " + uri);*/


        /*switch (match) {
            case BATCHES:
                // Consultando todos los registros
                c = db.query(DataLocalHelper.Tablas.BATCHES, projection,selection, selectionArgs,null, null, sortOrder);
                Log.e(TAG, "query: buscando0= ++++++++++++++++++++++++++++++++++++++" );
                break;
            case BATCHES_ROW:
                id = ContratosData.Batches.obtnerIdBatches(uri);
                c = db.query(DataLocalHelper.Tablas.BATCHES, projection,
                        ContratosData.ColumnasBatches.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                Log.e(TAG, "query: buscando1= ++++++++++++++++++++++++++++++++++++++" );
                break;
            case WAREHOUSE:
                Log.e(TAG, "query: warehose" );
                c = db.query(DataLocalHelper.Tablas.WAREHOUSE,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_WAREHOUSE);
                break;
            case WAREHOUSE_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                c = db.query(DataLocalHelper.Tablas.WAREHOUSE, projection,
                        ContratosData.ColumnasWarehouse.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_WAREHOUSE);
                break;
            case CORRALS:
                c =db.query(DataLocalHelper.Tablas.CORRALS,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_CORRALS);
                break;
            case CORRALS_ROW:
                id= ContratosData.Corrals.obtnerIdCorrals(uri);
                c = db.query(DataLocalHelper.Tablas.CORRALS, projection,
                        ContratosData.ColumnasCorrals.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_CORRALS);
                break;
            case WEIGHINGS:
                c =db.query(DataLocalHelper.Tablas.WEIGHINGS,projection,selection,selectionArgs,null,null,sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_WEIGHINGS);
                break;
            case WEIGHINGS_ROW:
                id= ContratosData.Weighings.obtnerIdWeighings(uri);
                c = db.query(DataLocalHelper.Tablas.WEIGHINGS, projection,
                        ContratosData.ColumnasWeighings.ID + " = " + id,
                        selectionArgs, null, null, sortOrder);
                c.setNotificationUri(resolver,ContratosData.CONTENT_URI_WEIGHINGS);
                break;
            default:
                throw new IllegalArgumentException("URI no soportada: " + uri);
        }
    c.setNotificationUri(resolver,uri);*/
        cursor.setNotificationUri(
                resolver,
                ContratosData.CONTENT_URI);
    return cursor;

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
                return  ContratosData.generarMime("corrals");
            case CORRALS_ROW:
                return  ContratosData.generarMimeItem("corrals");
            case WEIGHINGS:
                return  ContratosData.generarMime("weighings");
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
        SQLiteDatabase db = dataLocalHelper.getWritableDatabase();

        ContentValues contentValues;
        if (values != null) {
            contentValues = new ContentValues(values);
        } else {
            contentValues = new ContentValues();
        }

        if (uriMatcher.match(uri) == BATCHES){
            long rowid = db.insert(DataLocalHelper.Tablas.BATCHES,null,values);
            if (rowid > 0){
                Uri uri_lote = ContentUris.withAppendedId(ContratosData.CONTENT_URI,rowid);
                resolver.notifyChange(uri_lote,null,false);
                return uri_lote;
            }
        }
        if (uriMatcher.match(uri) == WAREHOUSE){
            long rowidw = db.insert(DataLocalHelper.Tablas.WAREHOUSE,null,values);
            if (rowidw > 0){
                Uri uri_lote = ContentUris.withAppendedId(ContratosData.CONTENT_URI_WAREHOUSE,rowidw);
                resolver.notifyChange(uri_lote,null,false);
                return uri_lote;
            }
        }
        if (uriMatcher.match(uri) == CORRALS){
            long rowid = db.insert(DataLocalHelper.Tablas.CORRALS,null,values);
            if (rowid > 0){
                Uri uri_lote = ContentUris.withAppendedId(ContratosData.CONTENT_URI_CORRALS,rowid);
                resolver.notifyChange(uri_lote,null,false);
                return uri_lote;
            }
        }
        throw new SQLException("Falla al insertar fila en : " + uri);

       /*switch (uriMatcher.match(uri)) {
            case BATCHES:
                db.insert(DataLocalHelper.Tablas.BATCHES, null, values);
                notificarCambio(uri);
                Log.d(TAG, "insert: insertando0 <> ++++++++++++++++++++++");
                return ContratosData.Batches.craerUriBatches(values.getAsString(DataLocalHelper.Tablas.BATCHES));
            case WAREHOUSE:
                db.insert(DataLocalHelper.Tablas.WAREHOUSE, null, values);
                notificarCambio(uri);
                return ContratosData.Warehouse.craerUriWarehouse(values.getAsString(DataLocalHelper.Tablas.WAREHOUSE));
            case CORRALS:
                db.insert(DataLocalHelper.Tablas.CORRALS, null, values);
                notificarCambio(uri);
                return ContratosData.Corrals.craerUriCorrals(values.getAsString(DataLocalHelper.Tablas.CORRALS));
            case WEIGHINGS:
                db.insert(DataLocalHelper.Tablas.WEIGHINGS, null, values);
                notificarCambio(uri);
                return ContratosData.Weighings.craerUriWeighings(values.getAsString(DataLocalHelper.Tablas.WEIGHINGS));
            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);

    }*/


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
        int affected;
       switch (uriMatcher.match(uri)){
           case BATCHES:
               affected = db.delete(DataLocalHelper.Tablas.BATCHES,selection,selectionArgs);
               Log.d(TAG, "delete: borrando0 <> -------------------------------------");
               break;
            case BATCHES_ROW:
                long id = ContentUris.parseId(uri);
                affected = db.delete(DataLocalHelper.Tablas.BATCHES,
                        ContratosData.Batches.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                Log.d(TAG, "delete: borrando1 <> -------------------------------------");
                resolver.notifyChange(uri,null,false);
                break;
           case WAREHOUSE:
               affected = db.delete(DataLocalHelper.Tablas.WAREHOUSE,selection,selectionArgs);
               System.out.println("------------------------"+"eliminando harehouse");
               break;
            case WAREHOUSE_ROW:
                long idw = ContentUris.parseId(uri);
                affected = db.delete(DataLocalHelper.Tablas.WAREHOUSE,
                        ContratosData.Warehouse.ID_REMOTA + "=" + idw
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                System.out.println("------------------------"+"eliminando warehouseRow");
                resolver.notifyChange(uri,null,false);
                break;
           case  CORRALS:
               affected = db.delete(DataLocalHelper.Tablas.CORRALS,selection,selectionArgs);
               break;
           /* case CORRALS_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                affected =  db.delete(DataLocalHelper.Tablas.CORRALS,
                        ContratosData.Corrals.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                resolver.notifyChange(uri,null,false);
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
                resolver.notifyChange(uri,null,false);
                break;*/

            default:
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);

        }
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
                Log.d(TAG, "update: actualizando0 <> +++++++++++++++++++++++++++++++++");
                break;
            case BATCHES_ROW:
                //id = ContratosData.Batches.obtnerIdBatches(uri);
                String idb = uri.getPathSegments().get(1);
                affected = db.update(DataLocalHelper.Tablas.BATCHES, values,
                        ContratosData.Batches.ID_REMOTA + "=" + idb
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                Log.d(TAG, "update: actualizando1 <> +++++++++++++++++++++++++++++++++");
                break;
            case WAREHOUSE:
                affected = db.update(DataLocalHelper.Tablas.WAREHOUSE, values,
                        selection, selectionArgs);
                break;
            case WAREHOUSE_ROW:
                id = ContratosData.Warehouse.obtnerIdWarehouse(uri);
                affected = db.update(DataLocalHelper.Tablas.WAREHOUSE, values,
                        ContratosData.Warehouse.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case CORRALS:
                affected = db.update(DataLocalHelper.Tablas.CORRALS,values,selection,selectionArgs);
                break;
            case CORRALS_ROW:
                id = ContratosData.Corrals.obtnerIdCorrals(uri);
                affected = db.update(DataLocalHelper.Tablas.CORRALS, values,
                        ContratosData.Corrals.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);
                break;
            case WEIGHINGS:
                affected = db.update(DataLocalHelper.Tablas.WEIGHINGS,values,selection,selectionArgs);
                break;
            case WEIGHINGS_ROW:
                id = ContratosData.Weighings.obtnerIdWeighings(uri);
                affected = db.update(DataLocalHelper.Tablas.WEIGHINGS, values,
                        ContratosData.Weighings.ID_REMOTA + "=" + id
                                + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""),
                        selectionArgs);

                break;
            default:
                Log.e(TAG, "update: "+ uri );
                throw new UnsupportedOperationException(URI_NO_SOPORTADA);
        }
        resolver.notifyChange(uri,null,false);
        return affected;

    }
}
