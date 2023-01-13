package com.example.humantechnologyproject;

public class Datos {
    private int imagen;
    private String titulo;
    private String descripcion;

    public Datos(int imagen, String titulo, String descripcion) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getImagen() {
        return imagen;
    }
}
