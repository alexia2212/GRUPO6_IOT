package com.example.grupo_iot.delactividad;

public class NuevoEventoLista {

        public String titulo;
        public String descripcion;

        public String fecha;

        public String lugar;
        public String foto;

        public String getTitulo() {
                return titulo;
        }

        public NuevoEventoLista(String titulo, String descripcion, String fecha, String lugar, String foto) {
                this.titulo = titulo;
                this.descripcion = descripcion;
                this.fecha = fecha;
                this.lugar = lugar;
                this.foto = foto;
        }

        public void setTitulo(String titulo) {
                this.titulo = titulo;
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
