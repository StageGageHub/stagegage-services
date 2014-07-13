package com.stagegage.services.repository;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.stagegage.services.dto.GenreDto;
import com.stagegage.services.repository.tables.GenreByNameRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Scott on 7/12/14.
 *
 * @author Scott Hendrickson
 */
@Repository
@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
public class GenreRepository {

    @Autowired
    private CqlTemplate template;

    @Autowired
    private Environment env;

    public GenreDto createOrSelect(GenreDto genreDto) {
        String select = QueryBuilder.select("genre", "id")
                                    .from("genres_by_name")
                                    .where(QueryBuilder.eq("genre", genreDto.getGenre())).toString();

        List<GenreByNameRow> genreRows = template.query(select, new RowMapper<GenreByNameRow>() {

            @Override
            public GenreByNameRow mapRow(Row row, int i) throws DriverException {
                return new GenreByNameRow(
                        row.getString("genre"),
                        row.getUUID("id")
                );
            }
        });

        if(genreRows == null || genreRows.isEmpty()) {
            String insert = QueryBuilder.insertInto(env.getProperty("cassandra.keyspace"), "genres_by_name")
                    .value("id", genreDto.getId())
                    .value("genre", genreDto.getGenre()).toString();

            template.execute(insert);
            return genreDto;
        } else {
            return GenreByNameRow.toDto(genreRows.get(0));
        }
    }
}
