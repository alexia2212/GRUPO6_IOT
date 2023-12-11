package com.example.grupo_iot.delactividad;

public class VistaPreviaActualizacionLista {

    public String nombre;
    public String fecha;

    public int imagen;
    public String descripcion;

    public String lugar;

    public VistaPreviaActualizacionLista(String nombre, String fecha, int imagen, String descripcion, String lugar) {
        this.nombre = nombre;
        this.fecha = fecha;
        this.imagen = imagen;
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


    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
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
