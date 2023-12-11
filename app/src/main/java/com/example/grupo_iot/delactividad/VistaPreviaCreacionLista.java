package com.example.grupo_iot.delactividad;

public class VistaPreviaCreacionLista {

    public String nombre;
    public String fecha;

    public int imagen1;
    public String descripcion;

    public String lugar;

    public VistaPreviaCreacionLista(String nombre, String fecha, int imagen1, String descripcion, String lugar) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.imagen1 = imagen1;
        this.descripcion = descripcion;
        this.lugar = lugar;
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
