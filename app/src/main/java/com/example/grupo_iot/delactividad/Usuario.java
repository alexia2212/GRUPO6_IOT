package com.example.grupo_iot.delactividad;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String nombre;

    private String apellido;
    private String condicion;
    private String funcion;

    private String foto;

    public Usuario() {
        // Deja el cuerpo del constructor vacío o inicializa cualquier campo necesario aquí
    }

    public Usuario(String nombre, String apellido, String condicion, String funcion, String foto) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.condicion = condicion;
        this.funcion = funcion;
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
