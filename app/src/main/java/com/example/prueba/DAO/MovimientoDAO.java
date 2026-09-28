package com.example.prueba.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.prueba.Database.DBHelper;
import com.example.prueba.clases.Movimiento;

import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    private final DBHelper dbHelper;

    public MovimientoDAO(Context context) {
        dbHelper = new DBHelper(context);
    }

    // ---------------------------------------------------------
    // Crear movimiento
    // ---------------------------------------------------------
    public long createMovimiento(Movimiento movimiento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("importe", movimiento.getImporte());
        valores.put("fecha", movimiento.getFecha());
        valores.put("descripcion", movimiento.getDescripcion());
        valores.put("tipo", movimiento.getTipo());
        valores.put("categoriaId", movimiento.getCategoriaId());

        long id = db.insert(DBHelper.TABLA_MOVIMIENTOS, null, valores);
        db.close();
        return id;
    }

    // ---------------------------------------------------------
    // Actualizar movimiento
    // ---------------------------------------------------------
    public int updateMovimiento(Movimiento movimiento) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put("importe", movimiento.getImporte());
        valores.put("fecha", movimiento.getFecha());
        valores.put("descripcion", movimiento.getDescripcion());
        valores.put("tipo", movimiento.getTipo());
        valores.put("categoriaId", movimiento.getCategoriaId());

        int filas = db.update(DBHelper.TABLA_MOVIMIENTOS, valores,
                "id = ?", new String[]{String.valueOf(movimiento.getId())});
        db.close();
        return filas;
    }

    // ---------------------------------------------------------
    // Eliminar movimiento
    // ---------------------------------------------------------
    public void removeMovimiento(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DBHelper.TABLA_MOVIMIENTOS, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ---------------------------------------------------------
    // Obtener movimientos de un mes (entre dos timestamps)
    // ---------------------------------------------------------
    public List<Movimiento> getMovimientosPorMes(long inicio, long fin) {
        List<Movimiento> movimientos = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLA_MOVIMIENTOS, null,
                "fecha BETWEEN ? AND ?",
                new String[]{String.valueOf(inicio), String.valueOf(fin)},
                null, null, "fecha DESC");

        while (cursor.moveToNext()) {
            movimientos.add(mapearMovimiento(cursor));
        }

        cursor.close();
        db.close();
        return movimientos;
    }

    // ---------------------------------------------------------
    // Obtener total de ingresos de un mes
    // ---------------------------------------------------------
    public double getTotalIngresos(long inicio, long fin) {
        return getTotalPorTipo("INGRESO", inicio, fin);
    }

    // ---------------------------------------------------------
    // Obtener total de gastos de un mes
    // ---------------------------------------------------------
    public double getTotalGastos(long inicio, long fin) {
        return getTotalPorTipo("GASTO", inicio, fin);
    }

    // ---------------------------------------------------------
    // Suma total por tipo (INGRESO o GASTO) en un rango de fechas
    // ---------------------------------------------------------
    private double getTotalPorTipo(String tipo, long inicio, long fin) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT SUM(importe) FROM " + DBHelper.TABLA_MOVIMIENTOS +
                        " WHERE tipo = ? AND fecha BETWEEN ? AND ?",
                new String[]{tipo, String.valueOf(inicio), String.valueOf(fin)});

        double total = 0;
        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        db.close();
        return total;
    }

    // ---------------------------------------------------------
    // Mapear una fila del cursor a un objeto Movimiento
    // ---------------------------------------------------------
    private Movimiento mapearMovimiento(Cursor cursor) {
        Movimiento movimiento = new Movimiento(
                cursor.getDouble(cursor.getColumnIndexOrThrow("importe")),
                cursor.getLong(cursor.getColumnIndexOrThrow("fecha")),
                cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                cursor.getString(cursor.getColumnIndexOrThrow("tipo")),
                cursor.getLong(cursor.getColumnIndexOrThrow("categoriaId"))
        );
        movimiento.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
        return movimiento;
    }

    // ---------------------------------------------------------
// Obtener movimiento por ID
// ---------------------------------------------------------
    public Movimiento getMovimientoById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(DBHelper.TABLA_MOVIMIENTOS, null,
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);

        Movimiento movimiento = null;
        if (cursor.moveToFirst()) {
            movimiento = mapearMovimiento(cursor);
        }

        cursor.close();
        db.close();
        return movimiento;
    }
}