package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    @ResponseBody
    public Optional<Artist> getArtist(@PathVariable(value = "id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if (optionalArtist.isPresent()){
            return optionalArtist;
        }
        throw new EntityNotFoundException("L'artiste d'identifiant" + id + " n'a pas été trouvé!");
    }

    /**
     * Permet de récuperer un artiste à partir se son nom
     *
     * @param name Le nom de l'artiste
     * @return l'artiste si le nom est trouvé, sinon une erreur
     */
    @RequestMapping(value = "", params = {"name"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Artist searchByName(@RequestParam("name") String name){
        Artist artist = artistRepository.findByName(name);
        if(artist == null){
            throw new EntityNotFoundException("L'artiste " + name + " n'a pas été trouvé");
        }
        return artist;
    }

    /**
     * Permet de récupérer les artistes de manière paginée et triée
     */
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Page<Artist> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.valueOf(sortDirection), sortProperty);
        if (page < 0) {
            //400
            throw new IllegalArgumentException("Le paramètre page doit être positif ou nul !");
        }
        if (size <= 0 || size > 50) {
            throw new IllegalArgumentException("Le paramètre size doit être compris entre 0 et 50");
        }
        if (!"ASC".equalsIgnoreCase(sortDirection) && !"DESC".equalsIgnoreCase(sortDirection)) {
            throw new IllegalArgumentException("Le paramètre sortDirection doit valoir ASC ou DESC");
        }
        return artistRepository.findAll(pageRequest);
    }
}
