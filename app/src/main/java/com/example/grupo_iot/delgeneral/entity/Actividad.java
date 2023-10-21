package com.example.grupo_iot.delgeneral.entity;

import java.io.Serializable;

public class Actividad implements Serializable {
    private String nombreActividad;
    private String descripcionActividad;
    private String delegadoActividad;

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
    public String getDelegadoActividad() {return delegadoActividad;
    }

    public void setDelegadoActividad(String delegadoActividad) {
        this.descripcionActividad = delegadoActividad;
    }


}
