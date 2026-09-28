package com.example.prueba.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prueba.Database.DBHelper;
import com.example.prueba.clases.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaDAO {

    private final DBHelper dbHelper;

    public CategoriaDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // ---------------------------------------------------------
    // Crear categoría
    // ---------------------------------------------------------
    public long createCategoria(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("nombre", categoria.getNombre());
        valores.put("tipo", categoria.getTipo());
        valores.put("color", categoria.getColor());

        long id = db.insert(DBHelper.TABLA_CATEGORIAS, null, valores);
        db.close();
        return id;
    }

    // ---------------------------------------------------------
    // Actualizar categoría
    // ---------------------------------------------------------
// ---------------------------------------------------------
// Actualizar categoría y sincronizar el tipo en sus movimientos
// ---------------------------------------------------------
    public int updateCategoria(Categoria categoria) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();

        int filas;
        try {
            ContentValues valores = new ContentValues();
            valores.put("nombre", categoria.getNombre());
            valores.put("tipo", categoria.getTipo());
            valores.put("color", categoria.getColor());

            filas = db.update(DBHelper.TABLA_CATEGORIAS, valores,
                    "id = ?", new String[]{String.valueOf(categoria.getId())});

            // Sincroniza el tipo en todos los movimientos que usan esta categoría
            ContentValues valoresMovimientos = new ContentValues();
            valoresMovimientos.put("tipo", categoria.getTipo());
            db.update(DBHelper.TABLA_MOVIMIENTOS, valoresMovimientos,
                    "categoriaId = ?", new String[]{String.valueOf(categoria.getId())});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }

        db.close();
        return filas;
    }

    // ---------------------------------------------------------
    // Eliminar categoría
    // ---------------------------------------------------------
    public void removeCategoria(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLA_CATEGORIAS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ---------------------------------------------------------
    // Obtener todas las categorías
    // ---------------------------------------------------------
    public List<Categoria> getAllCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLA_CATEGORIAS, null,
                null, null, null, null, "nombre ASC");

        while (cursor.moveToNext()) {
            categorias.add(mapearCategoria(cursor));
        }

        cursor.close();
        db.close();
        return categorias;
    }

    // ---------------------------------------------------------
    // Obtener categoría por ID
    // ---------------------------------------------------------
    public Categoria getCategoriaById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLA_CATEGORIAS, null,
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Categoria categoria = null;
        if (cursor.moveToFirst()) {
            categoria = mapearCategoria(cursor);
        }

        cursor.close();
        db.close();
        return categoria;
    }

    // ---------------------------------------------------------
    // Mapear una fila del cursor a un objeto Categoria
    // ---------------------------------------------------------
    private Categoria mapearCategoria(Cursor cursor) {
        Categoria categoria = new Categoria(
                cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cursor.getInt(cursor.getColumnIndexOrThrow("color"))
        );
        categoria.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        return categoria;
    }


}