package com.example.grupo_iot.delegadoGeneral.entity;

public class ActividadData {
    public ActividadData() {
    }

    private String nombreActividad;

    private String descripcionActividad;

    public ActividadData(String nombreActividad, String descripcionActividad) {
        this.nombreActividad = nombreActividad;
        this.descripcionActividad = descripcionActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public String getDescripcionActividad() {
        return descripcionActividad;
    }

    public void setDescripcionActividad(String descripcionActividad) {
        this.descripcionActividad = descripcionActividad;
    }
}
