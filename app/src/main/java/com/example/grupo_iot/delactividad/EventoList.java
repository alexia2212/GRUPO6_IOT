package com.example.grupo_iot.delactividad;

public class EventoList {
    public String nombre;
    public String fecha;

    public String imagen1;

    public String descripcion;

    public String lugar;


    public String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public EventoList(String nombre, String fecha, String imagen1, String descripcion, String lugar, String estado) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}