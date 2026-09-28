package com.example.prueba.clases;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static TipoMovimiento fromString(String value) {
        return value == null ? null : TipoMovimiento.valueOf(value);
    }

    @TypeConverter
    public static String toString(TipoMovimiento tipo) {
        return tipo == null ? null : tipo.name();
    }
}