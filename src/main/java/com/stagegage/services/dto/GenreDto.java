package com.stagegage.services.dto;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
public class GenreDto {

    private final UUID id;
    private final String genre;


    public GenreDto(UUID id, final String genre) {
        this.id = id;
        this.genre = genre;
    }

    public GenreDto(String genre) {
        this.id = UUID.randomUUID();
        this.genre = genre;
    }

    public static Set<GenreDto> toGenreSet(Map<UUID, String> genreMap) {
        if(genreMap == null) return null;

        Set<GenreDto> genres = new HashSet<GenreDto>();
        for(Map.Entry<UUID, String> entry : genreMap.entrySet()) {
            genres.add(new GenreDto(entry.getKey(), entry.getValue()));
        }

        return genres;
    }

    public UUID getId() {
        return id;
    }

    public String getGenre() {
        return genre;
    }
}
