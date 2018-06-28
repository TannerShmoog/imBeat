package com.app.imbeat;

import java.io.Serializable;

public class AudioFile implements Serializable {
    private String fileName;
    private String artistName;
    private String duration;
    private String data;

    public AudioFile(String data, String fileName, String artistName, String duration) {
        this.data = data;
        this.fileName = fileName;
        this.artistName = artistName;
        this.duration = duration;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
