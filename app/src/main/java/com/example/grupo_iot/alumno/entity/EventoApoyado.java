package com.example.grupo_iot.alumno.entity;

import java.io.Serializable;

public class EventoApoyado implements Serializable {
    private String evento;
    private String actividad;
    private String apoyo;

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getApoyo() {
        return apoyo;
    }

    public void setApoyo(String apoyo) {
        this.apoyo = apoyo;
    }
}
