package com.stagegage.services.dto.response;

import com.stagegage.services.dto.ArtistDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Scott on 7/12/14.
 */
public class ArtistResponse {

    private final UUID id;
    private final String name;
    private final Set<GenreResponse> genres;


    public static List<ArtistResponse> getResponses(List<ArtistDto> artistDtos) {
        if (artistDtos == null) return null;

        List<ArtistResponse> responses = new ArrayList<ArtistResponse>();
        for (ArtistDto dto : artistDtos) {
            responses.add(ArtistResponse.getResponse(dto));
        }

        return responses;
    }

    public static ArtistResponse getResponse(ArtistDto dto) {
        return new ArtistResponse(dto.getId(),
                                  dto.getName(),
                                  GenreResponse.getResponse(dto.getGenres()));
    }


    public ArtistResponse(UUID id, String name, Set<GenreResponse> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<GenreResponse> getGenres() {
        return genres;
    }
}
