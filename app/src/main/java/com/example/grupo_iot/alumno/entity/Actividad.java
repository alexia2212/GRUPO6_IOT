package com.example.grupo_iot.alumno.entity;

import java.io.Serializable;

public class Actividad implements Serializable {
    private Integer id;
    private String nombreActividad;
    private String descripcionActividad;
    private int imagenActividad;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getImagenActividad() {
        return imagenActividad;
    }

    public void setImagenActividad(int imagenActividad) {
        this.imagenActividad = imagenActividad;
    }
}
