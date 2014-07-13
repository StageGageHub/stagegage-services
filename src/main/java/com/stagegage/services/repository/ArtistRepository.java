package com.stagegage.services.repository;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.Assignment;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.stagegage.services.dto.ArtistDto;
import com.stagegage.services.dto.GenreDto;
import com.stagegage.services.repository.tables.ArtistByIdRow;
import com.stagegage.services.repository.tables.ArtistByNameRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
@Repository
@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
public class ArtistRepository {

    @Autowired
    private CqlTemplate template;

    @Autowired
    private Environment env;


    public List<ArtistDto> getAllArtists() {
        String select = QueryBuilder.select("id", "name", "genres").from("artists_by_name").toString();
        List<ArtistByNameRow> artistByNameRows = template.query(select, new RowMapper<ArtistByNameRow>() {

            @Override
            public ArtistByNameRow mapRow(Row row, int i) throws DriverException {
                return new ArtistByNameRow(
                        row.getUUID("id"),
                        row.getString("name"),
                        GenreDto.toGenreSet(row.getMap("genres", UUID.class, String.class))
                );
            }
        });

        return ArtistByNameRow.toDto(artistByNameRows);
    }

    public List<ArtistDto> getArtists(String genre) {

        return null;
    }

    public ArtistDto createArtist(ArtistDto artistDto) {
        String insert = QueryBuilder.insertInto(env.getProperty("cassandra.keyspace"), "artists_by_name")
                .value("id", artistDto.getId())
                .value("name", artistDto.getName())
                .value("genres", ArtistByIdRow.toGenreMap(artistDto.getGenres()))
                .toString();

        template.execute(insert);
        return artistDto;
    }

    public ArtistDto addArtistGenre(String name, GenreDto genreDto) {
        Assignment put = QueryBuilder.put("genres", genreDto.getId(), genreDto.getGenre());
        String update = QueryBuilder.update(env.getProperty("cassandra.keyspace"), "artists_by_name")
                                    .with(put)
                                    .where(QueryBuilder.eq("name", name)).toString();
        // Write new genre
        template.execute(update);

        // Now read out updated artist


        return getArtist(name);
    }

    public ArtistDto getArtist(String name) {
        String select = QueryBuilder.select("id", "name", "genres")
                .from("artists_by_name")
                .where(QueryBuilder.eq("name", name))
                .toString();
        List<ArtistByNameRow> artistByNameRows = template.query(select, new RowMapper<ArtistByNameRow>() {

            @Override
            public ArtistByNameRow mapRow(Row row, int i) throws DriverException {
                return new ArtistByNameRow(
                        row.getUUID("id"),
                        row.getString("name"),
                        GenreDto.toGenreSet(row.getMap("genres", UUID.class, String.class))
                );
            }
        });

        return ArtistByNameRow.toDto(artistByNameRows.get(0));
    }
}
