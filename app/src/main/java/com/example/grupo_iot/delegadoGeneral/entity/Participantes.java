package com.example.grupo_iot.delegadoGeneral.entity;

import java.io.Serializable;

public class Participantes implements Serializable {

    public String nombre;
    public String codigo;
    public String apellido;


    public Participantes() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
