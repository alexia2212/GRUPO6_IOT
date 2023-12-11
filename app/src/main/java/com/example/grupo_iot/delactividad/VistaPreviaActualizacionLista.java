package com.example.grupo_iot.delactividad;

public class VistaPreviaActualizacionLista {

    public String titulo;
    public String fecha;

    public int imagen1;
    public String descripcion;

    public String lugar;

    public VistaPreviaActualizacionLista(String titulo, String fecha, int imagen1, String descripcion, String lugar) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.lugar = lugar;
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

    public int getImagen1() {
        return imagen1;
    }

    public void setImagen1(int imagen1) {
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
