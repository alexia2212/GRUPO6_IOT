package com.example.grupo_iot.delactividad;

import java.io.Serializable;
import java.util.List;

public class Lista implements Serializable {
    public String nombre;
    public String fecha;

    public String imagen1;

    public String descripcion;

    public String lugar;

    public String nombreactividad;

    private List<String> urlsImagenes;

    public List<String> getUrlsImagenes() {
        return urlsImagenes;
    }

    public void setUrlsImagenes(List<String> urlsImagenes) {
        this.urlsImagenes = urlsImagenes;
    }

    public Lista(String nombre, String fecha, String imagen1, String descripcion, String lugar, String nombreactividad, List<String> urlsImagenes, String estado) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.nombreactividad = nombreactividad;
        this.urlsImagenes = urlsImagenes;
        this.estado = estado;
    }

    public String estado;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreactividad() {
        return nombreactividad;
    }

    public void setNombreactividad(String nombreactividad) {
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
}