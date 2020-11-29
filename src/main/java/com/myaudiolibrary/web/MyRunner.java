package com.myaudiolibrary.web;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Override
    public void run(String... args) throws Exception {
        Artist artist = artistRepository.findByArtistId(5);
        System.out.println(artist.getName());

        Album album = albumRepository.findByAlbumId(5);
        System.out.println(album.getTitle());
    }
}
