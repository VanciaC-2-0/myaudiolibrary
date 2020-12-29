package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import java.util.Optional;

//@CrossOrigin
//@RestController
@Controller
@RequestMapping(value= "/artists")
public class ArtistController {


    @Autowired
    private ArtistRepository artistRepository;

    @GetMapping(value= "/{id}")
    public String getArtistById(final ModelMap model, @PathVariable("id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        model.put("artist", optionalArtist.get());
        model.put("albumToCreate", new Album());
        return "detailArtist";
    }

    @GetMapping(value = "", params = "name")
    public String getByName(final ModelMap model,
                             @RequestParam(value ="name") String name,
                             @RequestParam(value = "page", defaultValue = "0") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
                             @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtist = artistRepository.findAllByNameContaining(name, pageRequest);
        model.put("size", size);
        model.put("sortProperty", sortProperty);
        model.put("sortDirection", sortDirection);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page)*size + pageArtist.getNumberOfElements());
        model.put("artists", pageArtist);
        return "listeArtists";
    }

    @GetMapping(value="")
    public String getListArtist(final ModelMap model,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                      @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
                                      @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
        Page<Artist> pageArtist = artistRepository.findAll(pageRequest);
        model.put("size", size);
        model.put("sortProperty", sortProperty);
        model.put("sortDirection", sortDirection);
        model.put("pageNumber", page + 1);
        model.put("previousPage", page - 1);
        model.put("nextPage", page + 1);
        model.put("start", page * size + 1);
        model.put("end", (page)*size + pageArtist.getNumberOfElements());
        model.put("artists", pageArtist);
        return "listeArtists";

    }
    /*

    /**
     * Permet de récupérer un artiste à partir de son identifiant
     *
     * @param id Identifiant de l'artiste
     * @return l'artiste si l'identifiant est trouvé, sinon erreur 404.
     */
    /*
    @RequestMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
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
    /*
    @RequestMapping(params = {"name"}, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page <Artist> getByName(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                   @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
                                   @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
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
        Page<Artist> listPageArtist= artistRepository.findAllByNameContaining(name, pageRequest);
        return listPageArtist;
    }

    /**
     * Permet de récupérer les artistes de manière paginée et triée
     * @param page Numéro de la page en partant de 0
     * @param size Taille de la page
     * @param sortDirection Tri ascendant ASC ou descendant DESC
     * @param sortProperty Propriété utilisée par le tri
     * @return Une page contenant les artistes
     */
    /*
    @RequestMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    public Page<Artist> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "sortProperty", defaultValue = "name") String sortProperty,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.fromString(sortDirection), sortProperty);
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

    //creation nouvel artiste
    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Artist createArtist(@RequestBody Artist artist){
        if(artistRepository.findByName(artist.getName()) != null){
            throw new EntityExistsException("Il y a déja un artiste de nom " + artist.getName());
        }
        return artistRepository.save(artist);
    }

    //modif artiste
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Artist updateArtist(@PathVariable Integer id, @RequestBody Artist artist){
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        return artistRepository.save(artist);
    }

    //Suppression artiste
    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)//204
    public void deleteArtist(@PathVariable Integer id){
        if(!artistRepository.existsById(id)){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé");
        }
        artistRepository.deleteById(id);
    }
     */
}
