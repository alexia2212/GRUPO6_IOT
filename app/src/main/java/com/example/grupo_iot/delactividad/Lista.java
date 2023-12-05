package com.example.grupo_iot.delactividad;

import java.io.Serializable;

public class Lista implements Serializable {
    public String titulo;
    public String fecha;

    public String imagen1;

    public String descripcion;

    public String lugar;

    public String nombreactividad;


    public String getNombreactividad() {
        return nombreactividad;
    }

    public void setNombreactividad(String nombreactividad) {
        this.nombreactividad = nombreactividad;
    }

    public Lista(String titulo, String fecha, String imagen1, String descripcion, String lugar, String nombreactividad) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.nombreactividad = nombreactividad;
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


    public Lista() {
        // Constructor sin argumentos
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
}