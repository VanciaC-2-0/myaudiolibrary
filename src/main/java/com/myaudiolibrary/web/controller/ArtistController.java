package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import com.myaudiolibrary.web.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Controller
@RequestMapping(value= "/artists")
public class ArtistController {


    @Autowired
    private ArtistRepository artistRepository;

    /**
     * Permet de récupérer un artiste à partir de son identifiant
     *
     * @param id Identifiant de l'artiste
     * @return La page de détails de l'artiste si l'identifiant est trouvé, sinon erreur 404.
     */
    @GetMapping(value= "/{id}")
    public String getArtistById(final ModelMap model, @PathVariable("id") Integer id){
        Optional<Artist> optionalArtist = artistRepository.findById(id);
        if(optionalArtist.isEmpty()){
            throw new EntityNotFoundException("L'artiste d'identifiant " + id + " n'a pas été trouvé !");
        }
        model.put("artist", optionalArtist.get());
        model.put("albumToCreate", new Album());
        return "detailArtist";
    }

    /**
     * Permet de récuperer un artiste à partir se son nom
     *
     * @param name Le nom de l'artiste
     * @return La page de la liste d'artiste en fonction du nom
     */
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

    /**
     * Permet de récupérer les artistes de manière paginée et triée
     * @param page Numéro de la page en partant de 0
     * @param size Taille de la page
     * @param sortDirection Tri ascendant ASC ou descendant DESC
     * @param sortProperty Propriété utilisée par le tri
     * @return La page de la liste d'artiste
     */
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

    /**
     *
     * @param model
     * @return La page de formulaire pour creer un nouvel artiste
     */
    @GetMapping(value = "/new")
    public String newArtist(final ModelMap model){
        model.put("artist", new Artist());
        model.put("album", new Album());
        return "detailArtist";
    }

    /**
     *
     * @param artist
     * @return la fonction de redirection après avoir créer ou modifier un artiste
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView createOrSaveArtist(Artist artist){
        return saveArtist(artist);
    }

    private RedirectView saveArtist(Artist artist){
        artist = artistRepository.save(artist);
        return new RedirectView("/artists/" + artist.getId());
    }

    /**
     *
     * @param id id de l'artiste
     * @return suppression de l'artiste et redirection vers la page de la liste des artistes
     */
    @GetMapping(value = "/{id}/delete")
    public RedirectView deleteArtist(@PathVariable (name="id") Integer id){
        artistRepository.deleteById(id);
        return new RedirectView("/artists");
    }
}
