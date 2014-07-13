package com.stagegage.services.repository.tables;

import com.stagegage.services.dto.ArtistDto;
import com.stagegage.services.dto.GenreDto;
import org.springframework.data.cassandra.mapping.Column;
import org.springframework.data.cassandra.mapping.PrimaryKey;
import org.springframework.data.cassandra.mapping.Table;

import java.util.*;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
@Table(value = "artists_by_id")
public class ArtistByIdRow {

    @PrimaryKey
    private final UUID id;

    @Column
    private final String name;
    @Column
    private final Map<UUID, String> genres;

    public ArtistByIdRow(final UUID id, final String name, final Map<UUID, String> genres) {
        this.id = id;
        this.name = name;
        this.genres = genres;
    }

    public ArtistByIdRow(final UUID id, final String name, final Set<GenreDto> genres) {
        this.id = id;
        this.name = name;

        if(genres == null) {
            this.genres = null;
        } else {
            this.genres = toGenreMap(genres);
        }
    }

    public static List<ArtistDto> toDto(List<ArtistByIdRow> artistRows) {
        if (artistRows == null) return null;

        List<ArtistDto> artistDtos = new ArrayList<ArtistDto>();
        for (ArtistByIdRow row : artistRows) {
            artistDtos.add(toDto(row));
        }

        return artistDtos;
    }

    public static ArtistDto toDto(ArtistByIdRow row) {
        return new ArtistDto(row.getId(), row.getName(), toGenreDtos(row.getGenres()));
    }


    private static Set<GenreDto> toGenreDtos(Map<UUID, String> genreMap) {
        if (genreMap == null) return null;

        Set<GenreDto> genresSet = new HashSet<GenreDto>();
        for (Map.Entry<UUID, String> entry : genreMap.entrySet()) {
            genresSet.add(new GenreDto(entry.getKey(), entry.getValue()));
        }

        return genresSet;
    }

    public static Map<UUID, String> toGenreMap(Set<GenreDto> genres) {
        if (genres == null) return null;

        Map<UUID, String> genresMap = new HashMap<UUID, String>();
        for (GenreDto genre : genres) {
            genresMap.put(genre.getId(), genre.getGenre());
        }

        return genresMap;
    }


    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<UUID, String> getGenres() {
        return genres;
    }
}
