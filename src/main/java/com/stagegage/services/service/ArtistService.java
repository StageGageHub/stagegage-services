package com.stagegage.services.service;

import com.stagegage.services.dto.ArtistDto;
import com.stagegage.services.dto.GenreDto;
import com.stagegage.services.repository.ArtistRepository;
import com.stagegage.services.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
@Component
public class ArtistService {

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreRepository genreRepository;

    public List<ArtistDto> getArtists(String genre) {
        if(genre != null) {
            return artistRepository.getArtists(genre);
        } else {
            return artistRepository.getAllArtists();
        }

    }

    public ArtistDto createArtist(ArtistDto artistDto) {
        return artistRepository.createArtist(artistDto);
    }

    public ArtistDto addGenreToArtist(String name, String genre) {

        GenreDto genreDto = genreRepository.createOrSelect(new GenreDto(genre));

        return artistRepository.addArtistGenre(name, genreDto);
    }

    public ArtistDto getArtist(String name) {
        return artistRepository.getArtist(name);
    }
}
