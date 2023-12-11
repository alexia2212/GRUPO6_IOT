package com.example.grupo_iot.alumno.entity;

import java.io.Serializable;
import java.util.Date;

public class Notificacion implements Serializable {

    private String contenido;
    private Date fechaHora;
    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
}
