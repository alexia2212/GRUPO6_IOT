package com.example.grupo_iot.alumno.entity;

import com.google.type.DateTime;

import java.io.Serializable;
import java.util.Date;

public class Evento implements Serializable {
    private String nombre;
    private String descripcion;
    private Date fechaHora;
    private String lugar;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getLugar() {
        return lugar;
    }
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
