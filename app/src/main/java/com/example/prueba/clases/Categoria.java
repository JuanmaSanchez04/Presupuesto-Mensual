package com.example.prueba.clases;

public class Categoria {

    private long id;
    private String nombre;
    private String tipo; // "INGRESO" o "GASTO"
    private int color;

    public Categoria(String nombre, String tipo, int color) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.color = color;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public int getColor() { return color; }
    public void setColor(int color) { this.color = color; }
}