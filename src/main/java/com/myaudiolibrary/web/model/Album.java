package com.myaudiolibrary.web.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class Album implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer albumId;

    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    private String title;

    public Album(){

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
}
