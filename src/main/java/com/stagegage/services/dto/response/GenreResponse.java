package com.stagegage.services.dto.response;

import com.stagegage.services.dto.GenreDto;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
public class GenreResponse {

    private final String genre;

    public GenreResponse(final String genre) {
        this.genre = genre;
    }

    public static Set<GenreResponse> getResponse(Set<GenreDto> genreDtos) {
        if (genreDtos == null) return null;

        Set<GenreResponse> responses = new HashSet<GenreResponse>();
        for (GenreDto dto : genreDtos) {
            responses.add(toResponse(dto));
        }

        return responses;
    }

    private static GenreResponse toResponse(GenreDto dto) {
        if(dto == null) return null;

        return new GenreResponse(dto.getGenre());
    }

    public String getGenre() {
        return genre;
    }
}
