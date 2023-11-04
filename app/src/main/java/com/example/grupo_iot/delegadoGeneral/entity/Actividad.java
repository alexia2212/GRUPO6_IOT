package com.example.grupo_iot.delegadoGeneral.entity;

import java.io.Serializable;

public class Actividad implements Serializable {
    public String nombreActividad;
    public String descripcionActividad;
    public String delegadoActividad;
    public Actividad(String nombreActividad, String descripcionActividad, String delegadoActividad) {
        this.nombreActividad = nombreActividad;
        this.descripcionActividad = descripcionActividad;
        this.delegadoActividad = delegadoActividad;
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

    public String getDelegadoActividad() {
        return delegadoActividad;
    }

    public void setDelegadoActividad(String delegadoActividad) {
        this.delegadoActividad = delegadoActividad;
    }
}
