package com.myaudiolibrary.web.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Artist implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int artistId;

    @OneToMany(mappedBy = "artist", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("artist")
    private Set<Album> albums = new HashSet<>();

    private String name;

    public Artist(){

    }

    public Artist(Set<Album> albums) {
        this.albums = albums;
    }

    public Set<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<Album> albums) {
        this.albums = albums;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
