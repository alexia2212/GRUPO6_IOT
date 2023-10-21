package com.example.grupo_iot.delactividad;

public class Usuario {
    private int imagenResId;
    private String nombre;
    private String condicion;
    private String funcion;

    public Usuario(int imagenResId, String nombre, String condicion, String funcion) {
        this.imagenResId = imagenResId;
        this.nombre = nombre;
        this.condicion = condicion;
        this.funcion = funcion;
    }

    // Agrega getters y setters según sea necesario

    public int getImagenResId() {
        return imagenResId;
    }

    public void setImagenResId(int imagenResId) {
        this.imagenResId = imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }
}
