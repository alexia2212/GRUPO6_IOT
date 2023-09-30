package com.example.grupo_iot.alumno.entity;

import java.io.Serializable;

public class Evento implements Serializable {
    private Integer id;
    private String nombreEvento;
    private String descripcionEvento;
    private int imagenEvento;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public String getDescripcionEvento() {
        return descripcionEvento;
    }

    public void setDescripcionEvento(String descripcionEvento) {
        this.descripcionEvento = descripcionEvento;
    }

    public int getImagenEvento() {
        return imagenEvento;
    }

    public void setImagenEvento(int imagenEvento) {
        this.imagenEvento = imagenEvento;
    }
}
