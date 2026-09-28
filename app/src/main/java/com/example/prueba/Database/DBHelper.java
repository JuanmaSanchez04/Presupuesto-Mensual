package com.example.prueba.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "presupuestador.db";
    private static final int DB_VERSION = 1;

    public static final String TABLA_CATEGORIAS = "categorias";
    public static final String TABLA_MOVIMIENTOS = "movimientos";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLA_CATEGORIAS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "tipo TEXT NOT NULL, " +
                "color INTEGER)");

        db.execSQL("CREATE TABLE " + TABLA_MOVIMIENTOS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "importe REAL NOT NULL, " +
                "fecha INTEGER NOT NULL, " +
                "descripcion TEXT, " +
                "tipo TEXT NOT NULL, " +
                "categoriaId INTEGER NOT NULL, " +
                "FOREIGN KEY(categoriaId) REFERENCES " + TABLA_CATEGORIAS + "(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_MOVIMIENTOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_CATEGORIAS);
        onCreate(db);
    }
}