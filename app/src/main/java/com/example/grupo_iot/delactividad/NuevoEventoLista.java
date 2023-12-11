package com.example.grupo_iot.delactividad;

public class NuevoEventoLista {

        public String nombre;
        public String descripcion;

        public String fecha;

        public String lugar;
        public String foto;

        public String getNombre() {
                return nombre;
        }

        public void setNombre(String nombre) {
                this.nombre = nombre;
        }

        public NuevoEventoLista(String nombre, String descripcion, String fecha, String lugar, String foto) {
                this.nombre = nombre;
                this.descripcion = descripcion;
                this.fecha = fecha;
                this.lugar = lugar;
                this.foto = foto;
        }


        public String getDescripcion() {
                return descripcion;
        }

        public void setDescripcion(String descripcion) {
                this.descripcion = descripcion;
        }

        public String getFecha() {
                return fecha;
        }

        public void setFecha(String fecha) {
                this.fecha = fecha;
        }

        public String getLugar() {
                return lugar;
        }

        public void setLugar(String lugar) {
                this.lugar = lugar;
        }

        public String getFoto() {
                return foto;
        }

        public void setFoto(String foto) {
                this.foto = foto;
        }
}
