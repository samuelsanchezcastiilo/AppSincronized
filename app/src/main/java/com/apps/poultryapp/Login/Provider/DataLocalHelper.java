package com.apps.poultryapp.Login.Provider;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;

public class DataLocalHelper extends SQLiteOpenHelper {
    private static final String NOMBRE_BASE_DATOS = "poultry.db";
    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    interface Tablas {
        String BATCHES = "batches";
        String CORRALS = "corrals";
        String WAREHOUSE = "warehouse";
        String WEIGHINGS = "weighings";
    }

    public DataLocalHelper(Context contexto) {
        super(contexto, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = contexto;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        /*db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT  NOT NULL, %s TEXT  NOT NULL, %s TEXT  NOT NULL," +
                        "%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL DEFAULT 0, %s TEXT NOT NULL %s)",
                Tablas.BATCHES, BaseColumns._ID,
                ContratosData.Batches.ID,
                ContratosData.Batches.NAME,
                ContratosData.Batches.COMPANY,
                ContratosData.Batches.FINALIZED,
                ContratosData.Batches.CREATE,
                ContratosData.Batches.UPDATE,
                ContratosData.Batches.ID_REMOTA,
                ContratosData.Batches.ESTADO,
                ContratosData.Batches.PENDIENTE_INSERCION
        ));*/

        db.execSQL("CREATE TABLE "+ Tablas.BATCHES + " (" +
                ContratosData.Batches.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContratosData.Batches.NAME + " TEXT, "+
                ContratosData.Batches.COMPANY + " TEXT, " +
                ContratosData.Batches.FINALIZED + " TEXT, " +
                ContratosData.Batches.CREATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Batches.UPDATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Batches.ID_REMOTA + " TEXT UNIQUE," +
                ContratosData.Batches.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContratosData.ESTADO_OK+"," +
                ContratosData.Batches.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)");


        db.execSQL("CREATE TABLE "+ Tablas.WAREHOUSE + " (" +
                ContratosData.Warehouse.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContratosData.Warehouse.NAME + " TEXT, "+
                ContratosData.Warehouse.BATCH + " TEXT, " +
                ContratosData.Warehouse.COMPANY + " TEXT, " +
                ContratosData.Warehouse.CREATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Warehouse.UPDATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Warehouse.ID_REMOTA + " TEXT UNIQUE," +
                ContratosData.Warehouse.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContratosData.ESTADO_OK+"," +
                ContratosData.Warehouse.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)");


        db.execSQL("CREATE TABLE "+ Tablas.CORRALS + " (" +
                ContratosData.Corrals.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContratosData.Corrals.NAME + " TEXT, "+
                ContratosData.Corrals.AGE + " TEXT, " +
                ContratosData.Corrals.CREATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Corrals.UPDATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Corrals.ID_REMOTA + " TEXT UNIQUE," +
                ContratosData.Corrals.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContratosData.ESTADO_OK+"," +
                ContratosData.Corrals.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)");

        db.execSQL("CREATE TABLE "+ Tablas.WEIGHINGS + " (" +
                ContratosData.Weighings.ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContratosData.Weighings.NAME + " TEXT, "+
                ContratosData.Weighings.BRIDS + " TEXT, " +
                ContratosData.Weighings.AGE + " TEXT, " +
                ContratosData.Weighings.CREATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Weighings.UPDATE+ " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                ContratosData.Weighings.ID_REMOTA + " TEXT UNIQUE," +
                ContratosData.Weighings.ESTADO + " INTEGER NOT NULL DEFAULT "+ ContratosData.ESTADO_OK+"," +
                ContratosData.Weighings.PENDIENTE_INSERCION + " INTEGER NOT NULL DEFAULT 0)");







        /*db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT  NOT NULL, %s TEXT  NOT NULL,%s INTEGER  NOT NULL," +
                        "%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL DEFAULT 0, %s TEXT NOT NULL %s)",
                Tablas.CORRALS, BaseColumns._ID,
                ContratosData.Corrals.ID,
                ContratosData.Corrals.NAME,
                ContratosData.Corrals.WAREHOUSE,
                ContratosData.Corrals.AGE,
                ContratosData.Corrals.CREATE,
                ContratosData.Corrals.UPDATE,
                ContratosData.Corrals.ID_REMOTA,
                ContratosData.Corrals.ESTADO,
                ContratosData.Corrals.PENDIENTE_INSERCION
        ));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT  NOT NULL, %s TEXT  NOT NULL, " +
                        "%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL DEFAULT 0, %s TEXT NOT NULL %s)",
                Tablas.WAREHOUSE, BaseColumns._ID,
                ContratosData.Warehouse.ID,
                ContratosData.Warehouse.NAME,
                ContratosData.Warehouse.BATCH,
                ContratosData.Warehouse.CREATE,
                ContratosData.Warehouse.UPDATE,
                ContratosData.Warehouse.ID_REMOTA,
                ContratosData.Warehouse.ESTADO,
                ContratosData.Warehouse.PENDIENTE_INSERCION

        ));
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT  NOT NULL, %s TEXT  NOT NULL, %s INTEGER NOT NULL, " +
                        "%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,%s  TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                        "%s TEXT UNIQUE NOT NULL,%s INTEGER NOT NULL DEFAULT 0, %s TEXT NOT NULL %s)",
                Tablas.WEIGHINGS, BaseColumns._ID,
                ContratosData.Weighings.ID,
                ContratosData.Weighings.NAME,
                ContratosData.Weighings.BRIDS,
                ContratosData.Weighings.AGE,
                ContratosData.Weighings.CREATE,
                ContratosData.Weighings.UPDATE,
                ContratosData.Weighings.ID_REMOTA,
                ContratosData.Weighings.ESTADO,
                ContratosData.Weighings.PENDIENTE_INSERCION));*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.BATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.CORRALS);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.WAREHOUSE);
        db.execSQL("DROP TABLE IF EXISTS " + Tablas.WEIGHINGS);
        onCreate(db);
    }


}
