package com.example.lenovo.thejamroom.pojo;


/**
 * Created by Avaneesh on 24/10/2014.
 */

public class Song {
    String name;
    String artist;
    String genre;
    String location;
    String path;
    public Song() {
    }

    public Song(String name, String artist, String genre, String location, String path ){
        this.name = name;
        this.artist = artist;
        this.genre = genre;
        this.location = location;
        this.path = path;
    }
    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {return path; }

    public void setPath(String path) { this.path = path; }

}
