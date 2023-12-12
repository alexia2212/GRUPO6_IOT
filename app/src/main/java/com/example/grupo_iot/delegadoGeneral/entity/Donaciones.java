package com.example.grupo_iot.delegadoGeneral.entity;

import java.io.Serializable;

public class Donaciones implements Serializable {
    String condicion;
    String monto;

    public Donaciones() {
    }

    public Donaciones(String condicion, String monto) {
        this.condicion = condicion;
        this.monto = monto;
    }
    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getMonto() {
        return monto;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }
}
