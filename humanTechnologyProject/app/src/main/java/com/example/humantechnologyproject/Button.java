package com.example.humantechnologyproject;

public class Button {
    private String imagen;
    private String titulo;
    private String audio;
    private String color;
    private int screenTime;
    private int audioTime;
    //private int edit_button;

    public Button() {

    }
    public Button(String imagen, String titulo, String audio, String color) {
        this.imagen = imagen;
        this.titulo = titulo;
        this.audio = audio;
        this.color = color;
        //this.edit_button = edit_button;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setColor(String color) {
        this.color = color;
    }


    public String getTitulo() {
        return titulo;
    }

    public String getAudio() {
        return audio;
    }

    public String getImagen() {
        return imagen;
    }

    //public int getEdit_button() {return edit_button;}

    public String getColor() {
        return color;
    }

    public void setScreenTime(int screenTime) {
        this.screenTime = screenTime;
    }

    public void setAudioTime(int audioTime) {
        this.audioTime = audioTime;
    }

    public int getScreenTime() {
        return screenTime;
    }

    public int getAudioTime() {
        return audioTime;
    }

}
