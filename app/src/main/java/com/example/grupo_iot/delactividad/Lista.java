package com.example.grupo_iot.delactividad;

public class Lista {
    public String titulo;
    public String fecha;

    public String imagen1;

    public Lista(String titulo, String fecha, String imagen1) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.imagen1 = imagen1;

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