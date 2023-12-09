package com.example.grupo_iot.delactividad;

public class EventoList {
    public String titulo;
    public String fecha;

    public String imagen1;

    public String descripcion;

    public String ubicacion;


    public String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public EventoList(String titulo, String fecha, String imagen1, String descripcion, String ubicacion, String estado) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImagen1() {
        return imagen1;
    }

    public void setImagen1(String imagen1) {
        this.imagen1 = imagen1;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}