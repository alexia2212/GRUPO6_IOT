package com.example.grupo_iot.delegadoGeneral.entity;

public class DataHolder {
    private static DataHolder instance;
    private String nombreActividad;

    private DataHolder() {
        // Constructor privado para evitar instanciación directa
    }

    public static synchronized DataHolder getInstance() {
        if (instance == null) {
            instance = new DataHolder();
        }
        return instance;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }
}
