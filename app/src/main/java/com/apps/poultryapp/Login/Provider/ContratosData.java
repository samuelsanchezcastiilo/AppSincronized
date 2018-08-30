package com.apps.poultryapp.Login.Provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContratosData {

    interface ColumnasBatches{
        String ID = "id";
        String NAME = "name";
        String COMPANY = "company";
        String FINALIZED = "finalized";
        String CREATE = "created_at";
        String UPDATE = "updated_at";

        String ESTADO = "estado";
        String ID_REMOTA = "idRemota";
        String PENDIENTE_INSERCION = "pendiente_insercion";

    }
    interface ColumnasWarehouse{
        String ID = "id";
        String NAME = "name";
        String BATCH = "batch";
        String COMPANY = "company";
        String CREATE = "created_at";
        String UPDATE = "updated_at";

        String ESTADO = "estado";
        String ID_REMOTA = "idRemota";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }

    interface  ColumnasCorrals{

        String ID = "id";
        String NAME = "name";
        String WAREHOUSE = "warehouse";
        String AGE = "age";
        String COMPANY = "company";
        String CREATE = "created_at";
        String UPDATE = "updated_at";
        String ESTADO = "estado";
        String ID_REMOTA = "idRemota";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }

    interface ColumnasWeighings{
        String ID = "id";
        String NAME = "name";
        String BRIDS = "brids";
        String AGE = "age";
        String COMPANY = "company";
        String CORRAL = "corral";
        String CREATE = "created_at";
        String UPDATE = "updated_at";

        String ESTADO = "estado";
        String ID_REMOTA = "idRemota";
        String PENDIENTE_INSERCION = "pendiente_insercion";
    }







    /**
     * Autoridad del Content Provider
     */
    public final static String AUTHORITY
            = "com.apps.poultryapp";

    public static final Uri URI_BASE = Uri.parse("content://" + AUTHORITY);


    public static final String RUTA_BATCHES = "batches";
    public static final String RUTA_CORRALS = "corrals";
    public static final String RUTA_WAREHOUSE = "warehouse";
    public static final String RUTA_WEIGHINGS = "weighings";

    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + RUTA_BATCHES);

    public final static Uri CONTENT_URI_WAREHOUSE =
            Uri.parse("content://" + AUTHORITY + "/" + RUTA_WAREHOUSE);

    public final static Uri CONTENT_URI_CORRALS=
            Uri.parse("content://" + AUTHORITY + "/" + RUTA_CORRALS);

    public final static Uri CONTENT_URI_WEIGHINGS =
            Uri.parse("content://" + AUTHORITY + "/" + RUTA_WEIGHINGS);




    public static final String BASE_CONTENIDOS = "poultryapp.";

    public static final String TIPO_CONTENIDO = "vnd.android.cursor.dir/vnd."
            + BASE_CONTENIDOS;

    public static final String TIPO_CONTENIDO_ITEM = "vnd.android.cursor.item/vnd."
            + BASE_CONTENIDOS;

    public static String generarMime(String id) {
        if (id != null) {
            return TIPO_CONTENIDO + id;
        } else {
            return null;
        }
    }

    public static String generarMimeItem(String id) {
        if (id != null) {
            return TIPO_CONTENIDO_ITEM + id;
        } else {
            return null;
        }
    }

    // Valores para la columna ESTADO
    public static final int ESTADO_OK = 0;
    public static final int ESTADO_SYNC = 1;


    public static class Batches implements ColumnasBatches{

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_BATCHES).build();

        public static  Uri craerUriBatches(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }
        public static  String obtnerIdBatches(Uri uri){
            return uri.getLastPathSegment();
        }



    }

    public static class Warehouse implements ColumnasWarehouse{
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_WAREHOUSE).build();


        public static  Uri craerUriWarehouse(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

        public static  String obtnerIdWarehouse(Uri uri){
            return uri.getLastPathSegment();
        }



    }

    public static class Corrals implements  ColumnasCorrals{

        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_CORRALS).build();

        public static  String obtnerIdCorrals(Uri uri){
            return uri.getLastPathSegment();
        }
        public static  Uri craerUriCorrals(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

    }




    public static class Weighings implements ColumnasWeighings{
        public static final Uri URI_CONTENIDO =
                URI_BASE.buildUpon().appendPath(RUTA_WEIGHINGS).build();
        public static  String obtnerIdWeighings(Uri uri){
            return uri.getLastPathSegment();
        }
        public static  Uri craerUriWeighings(String id){
            return URI_CONTENIDO.buildUpon().appendPath(id).build();
        }

    }

private ContratosData(){

}
}
