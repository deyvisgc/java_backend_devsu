package com.example.prueba_tecnica.util;

public enum TipoMovimiento {
    DEPOSITO("Deposito"),
    RETIRO("Retiro");

    private final String descripcion;

    TipoMovimiento(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getDescripcion() {
        return descripcion;
    }
}
