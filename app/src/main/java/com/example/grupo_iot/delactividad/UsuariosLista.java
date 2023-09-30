package com.example.grupo_iot.delactividad;

public class UsuariosLista {

    public String nombre;
    public String condicion;

    public String funcion;

    public UsuariosLista(String nombre, String condicion, String funcion, int img1) {
        this.nombre = nombre;
        this.condicion = condicion;
        this.funcion = funcion;
        this.img1 = img1;
    }

    public int img1;

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

    public int getImg1() {
        return img1;
    }

    public void setImg1(int img1) {
        this.img1 = img1;
    }
}
