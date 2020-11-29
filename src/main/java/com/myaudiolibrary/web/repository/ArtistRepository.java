package com.myaudiolibrary.web.repository;

import com.myaudiolibrary.web.model.Album;
import com.myaudiolibrary.web.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Integer> {
    Artist findByName(String name);
}
