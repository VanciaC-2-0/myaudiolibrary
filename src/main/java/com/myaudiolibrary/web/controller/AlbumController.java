package com.myaudiolibrary.web.controller;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@CrossOrigin
@RestController
@RequestMapping("/albums")
public class AlbumController {
    @Autowired
    private AlbumRepository albumRepository;

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Album addAlbum(@RequestBody Album album){
        if(albumRepository.findByTitle(album.getTitle()) != null){
            throw new EntityExistsException("Il y a déja un album de nom " + album.getTitle());
        }
        return albumRepository.save(album);
    }

    @RequestMapping(value ="/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.NO_CONTENT)//204
    public void deleteAlbum(@PathVariable Integer id){
        if(!albumRepository.existsById(id)){
            throw new EntityNotFoundException("L'album d'identifiant " + id + " n'a pas été trouvé");
        }
        albumRepository.deleteById(id);
    }
}
