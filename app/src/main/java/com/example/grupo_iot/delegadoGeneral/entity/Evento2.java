package com.example.grupo_iot.delegadoGeneral.entity;

import java.io.Serializable;
import java.util.Date;

public class Evento2 implements Serializable {

    public String descripcion;
    public Date fechaHora;
    public String lugar;
    public String nombre;
    public String imagen;

    public Evento2() {
    }

    public Evento2(String nombre, String descripcion, Date fechaHora, String lugar, String imagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaHora = fechaHora;
        this.lugar = lugar;
        this.imagen = imagen;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
