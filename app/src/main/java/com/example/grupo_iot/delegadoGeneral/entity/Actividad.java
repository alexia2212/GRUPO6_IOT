package com.example.grupo_iot.delegadoGeneral.entity;

import java.io.Serializable;

public class Actividad implements Serializable {
    public String nombreActividad;

    public String descripcionActividad;
    public String delegadoActividad;
    public String emailDelegado;

    public String id;
    public Actividad(String nombreActividad, String descripcionActividad, String delegadoActividad, String emailDelegado, String id) {
        this.nombreActividad = nombreActividad;
        this.descripcionActividad = descripcionActividad;
        this.delegadoActividad = delegadoActividad;
        this.emailDelegado = emailDelegado;
        this.id = id;
    }

    public Actividad() {
    }

    public String getEmailDelegado() {
        return emailDelegado;
    }

    public void setEmailDelegado(String emailDelegado) {
        this.emailDelegado = emailDelegado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDelegadoActividad() {
        return delegadoActividad;
    }

    public void setDelegadoActividad(String delegadoActividad) {
        this.delegadoActividad = delegadoActividad;
    }
}
