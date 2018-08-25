package com.apps.poultryapp.Login.DataLocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataLocal extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "vehicles.db";
    public static final String TABLE_LOTES = "batches";

    public DataLocal(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

       /*String Sql = "CREATE TABLE "+ ContractParaGastos.GASTO + " (" +
                ContractParaGastos.Columnas._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractParaGastos.Columnas.MONTO + " TEXT, "+
                ContractParaGastos.Columnas.ETIQUETA + " TEXT, " +
                ContractParaGastos.Columnas.FECHA + " TEXT, " +
                ContractParaGastos.Columnas.DESCRIPCION + " TEXT," +
                ContractParaGastos.Columnas.ID_REMOTA + " TEXT UNIQUE," +
                ContractParaGastos.Columnas.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContractParaGastos.ESTADO_OK+"," +
                ContractParaGastos.Columnas.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)";*/

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
