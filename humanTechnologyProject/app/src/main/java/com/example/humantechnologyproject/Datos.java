package com.example.humantechnologyproject;

public class Datos {
    private int imagen;
    private String titulo;
    private String audio;
    private int numButton;
    //private int edit_button;


    public Datos(int imagen, String titulo, String audio, int numButton) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.audio = audio;
        this.numButton = numButton;
        //this.edit_button = edit_button;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAudio() {
        return audio;
    }

    public int getImagen() {
        return imagen;
    }

    //public int getEdit_button() {return edit_button;}

    public int getNumButton() {
        return numButton;
    }
}
