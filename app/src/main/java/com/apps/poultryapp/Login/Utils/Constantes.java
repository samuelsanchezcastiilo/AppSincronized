package com.apps.poultryapp.Login.Utils;

public class Constantes {



    /**
     * Dirección IP de genymotion o AVD
     */
    //private static final String IP = ;
    public static final String IP = "http://poultry.spacetouch.tech";

    /**
     * URLs del Web Service
     */
    public static final String GET_URL = IP + "/api/getBaches";
    public static final String INSERT_URL = IP + "/api/savebatch";

    public static final String GET_URL_WAREHOUSE = IP + "/api/getWarehouse";
    public static final String INSERT_URL_WAREHOUSE = IP + "/api/savewarehouse";
    /**
     * Campos de las respuestas Json
     */
/* campos para la tabla lote */
    public static final String ID = "id";
    public static final String ESTADO = "estado";



    public static final String BATCHES = "batches";
    public static final String WAREHOUSE = "warehouse";
    public static final String WEIGHIGS = "weighings";
    public static final String CORRALS = "corrals";


    public static final String MENSAJE = "mensaje";

    public static final String SUCCESS = "1";
    public static final String FAILED = "2";

    /**
     * Tipo de cuenta para la sincronización
     */
    public static final String ACCOUNT_TYPE = "com.apps.poultryapp.account";

}
