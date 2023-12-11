package com.example.grupo_iot.delactividad;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Lista implements Serializable {
    public String nombre;

    public String imagen;

    public String descripcion;

    public String lugar;

    public String nombreactividad;

    private Date fechaHora;

    private List<String> urlsImagenes;

    public List<String> getUrlsImagenes() {
        return urlsImagenes;
    }

    public void setUrlsImagenes(List<String> urlsImagenes) {
        this.urlsImagenes = urlsImagenes;
    }

    public Lista(String nombre, String imagen, String descripcion, String lugar, String nombreactividad, Date fechaHora, List<String> urlsImagenes, String estado) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.descripcion = descripcion;
        this.lugar = lugar;
        this.nombreactividad = nombreactividad;
        this.fechaHora = fechaHora;
        this.urlsImagenes = urlsImagenes;
        this.estado = estado;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}