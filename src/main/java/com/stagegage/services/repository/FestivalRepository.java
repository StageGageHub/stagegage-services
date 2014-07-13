package com.stagegage.services.repository;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.exceptions.DriverException;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.stagegage.services.dto.FestivalDto;
import com.stagegage.services.repository.tables.FestivalByNameRow;
import com.stagegage.services.repository.tables.FestivalShowByNameIdRow;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cassandra.core.CqlTemplate;
import org.springframework.cassandra.core.RowMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Scott on 7/11/14.
 *
 * @author Scott Hendrickson
 */
@Repository
@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
public class FestivalRepository {

    @Autowired
    private CqlTemplate template;

    @Autowired
    private Environment env;

    public List<FestivalDto> getFestivals(final String name, String genre) {
        return null;
    }

    public List<FestivalDto>getAllFestivals() {
        String select = QueryBuilder.select("id", "name", "start_date", "end_date").from("festivals_by_name").toString();
        List<FestivalByNameRow> festivalRows = template.query(select, new RowMapper<FestivalByNameRow>() {
            @Override
            public FestivalByNameRow mapRow(Row row, int i) throws DriverException {
                return new FestivalByNameRow(
                    row.getUUID("id"),
                    row.getString("name"),
                    new DateTime(row.getDate("start_date")),
                    new DateTime(row.getDate("end_date"))
                );
            }
        });

        return FestivalByNameRow.toDto(festivalRows);
    }

    public FestivalDto createFestival(FestivalDto festivalDto) {
        String insert = QueryBuilder.insertInto(env.getProperty("cassandra.keyspace"), "festivals_by_name")
                            .value("id", festivalDto.getId())
                            .value("name", festivalDto.getName())
                            .value("start_date", festivalDto.getStartDate().toDateTime().toString())
                            .value("end_date", festivalDto.getEndDate().toDateTime().toString())
                            .toString();

        template.execute(insert);
        return festivalDto;
    }

    public FestivalDto getFestivalByName(String festivalName) {
        String select = QueryBuilder.select("id", "name", "start_date", "end_date").from("festivals_by_name").toString();
        List<FestivalByNameRow> festivalRows = template.query(select, new RowMapper<FestivalByNameRow>() {
            @Override
            public FestivalByNameRow mapRow(Row row, int i) throws DriverException {
                return new FestivalByNameRow(
                        row.getUUID("id"),
                        row.getString("name"),
                        new DateTime(row.getDate("start_date")),
                        new DateTime(row.getDate("end_date"))
                );
            }
        });

        return FestivalByNameRow.toDto(festivalRows).get(0);
    }

    public FestivalDto getFestivalShows(String festivalName) {
        String select = QueryBuilder.select("id", "name", "start_date", "end_date").from("festivals_by_name").toString();
        List<FestivalShowByNameIdRow> festivalRows = template.query(select, new RowMapper<FestivalShowByNameIdRow>() {

            @Override
            public FestivalShowByNameIdRow mapRow(Row row, int i) throws DriverException {
                return new FestivalShowByNameIdRow(
                        row.getString("festival_name"),
                        row.getUUID("show_id"),
                        row.getString("artist_name"),
                        new DateTime(row.getDate("start_date")),
                        new DateTime(row.getDate("end_date"))
                );
            }
        });

        return FestivalShowByNameIdRow.toDto(festivalRows).get(0);
    }
}
