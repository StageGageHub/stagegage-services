package com.stagegage.services.dto;

import java.util.Set;
import java.util.UUID;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
public class ArtistDto {

    private final UUID id;
    private final String name;
    private final Set<GenreDto> genres;

    public ArtistDto(UUID id, String name, Set<GenreDto> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public ArtistDto(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        genres = null;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<GenreDto> getGenres() {
        return genres;
    }


}
