package com.example.humantechnologyproject;

public class Button {
    private int id;
    private String image;
    private String title;
    private String audio;
    private String color;
    private int screenTime;
    private int audioTime;

    public Button() {

    }

    public Button(String image, String title, String audio, String color) {
        this.image = image;
        this.title = title;
        this.audio = audio;
        this.color = color;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public String getAudio() {
        return audio;
    }

    public String getImage() {
        return image;
    }

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

    public int getId() { return id;}

    public void setId(int id) { this.id = id; }
    @Override
    public String toString() {
        return "Button{" +
                "id=" + id +
                ", image='" + image + '\'' +
                ", title='" + title + '\'' +
                ", audio='" + audio + '\'' +
                ", color='" + color + '\'' +
                ", screenTime=" + screenTime +
                ", audioTime=" + audioTime +
                '}';
    }
}
