package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping(value= "/albums")
public class AlbumController {

    @Autowired
    private AlbumRepository albumRepository;


    /**
     *
     * @param album
     * @param artist
     * @return la fonction de redirection quand l'album a été créer
     */
    @PostMapping(consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createAlbum(Album album, Artist artist){
        return saveAlbum(album, artist);
    }

    private RedirectView saveAlbum(Album album, Artist artist){
        albumRepository.save(album);
        return new RedirectView("/artists/" + artist.getId());
    }

    /**
     *
     * @param id Id de l'album
     * @return La suppression de l'album si l'ID est trouvé, sinon erreur 404
     */
    @GetMapping(value = "/{id}")
    public RedirectView deleteAlbum(@PathVariable(name = "id") Integer id){
        if(!albumRepository.existsById(id)){
            throw new EntityNotFoundException("L'album d'identifiant " + id + " n'a pas été trouvé");
        }
        //recupere l'id de l'artiste
        Optional<Album> albumId = albumRepository.findById(id);
        Integer artistId = albumId.get().getArtist().getId();
        albumRepository.deleteById(id);
        return new RedirectView("/artists/" + artistId);
    }
}
