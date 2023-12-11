package com.example.grupo_iot.delactividad;

import java.util.Date;

public class EventoList {
    public String nombre;
    public Date fechaHora;

    public String imagen;

    public String descripcion;

    public String lugar;


    public String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public EventoList(String nombre, Date fechaHora, String imagen, String descripcion, String lugar, String estado) {
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}