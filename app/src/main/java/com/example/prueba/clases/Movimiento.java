package com.example.prueba.clases;

public class Movimiento {

    private long id;
    private double importe;
    private long fecha;
    private String descripcion;
    private String tipo; // "INGRESO" o "GASTO"
    private long categoriaId;

    public Movimiento(double importe, long fecha, String descripcion, String tipo, long categoriaId) {
        this.importe = importe;
        this.fecha = fecha;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoriaId = categoriaId;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public long getFecha() { return fecha; }
    public void setFecha(long fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(long categoriaId) { this.categoriaId = categoriaId; }
}