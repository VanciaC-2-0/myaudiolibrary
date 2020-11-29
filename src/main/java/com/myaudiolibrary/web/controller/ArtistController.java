package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/artists")
public class ArtistController {
    @Autowired
    ArtistRepository artistRepository;

    /**
     * Permet de récupérer un artiste à partir de son identifiant
     *
     * @param id Identifiant de l'artiste
     * @return l'artiste si l'identifiant est trouvé, sinon erreur 404.
     */
    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Optional<Artist> getArtist(@PathVariable(value = "id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()){
            System.out.println(optionalArtist.get().getName());
            return optionalArtist;
        }
        throw new EntityNotFoundException("L'artiste d'identifiant" + id + " n'a pas été trouvé!");
    }

    /**
     * Permet de récuperer un artiste à partir se son nom
     *
     */
    @RequestMapping(params = {"name"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist searchByName(@RequestParam("name") String name){
        Artist artist = artistRepository.findByName(name);
        if(artist == null){
            throw new EntityNotFoundException("L'artiste " + name + " n'a pas été trouvé");
        }
        return artist;
    }
}
