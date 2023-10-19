package com.example.grupo_iot.alumno.entity;

import java.io.Serializable;

public class Actividad implements Serializable {
    private String nombreActividad;
    private String descripcionActividad;
    private String idImagenActividad;

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

    public String getIdImagenActividad() {
        return idImagenActividad;
    }

    public void setIdImagenActividad(String idImagenActividad) {
        this.idImagenActividad = idImagenActividad;
    }
}
