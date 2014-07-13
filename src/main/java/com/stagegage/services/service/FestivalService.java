package com.stagegage.services.service;

import com.stagegage.services.dto.FestivalDto;
import com.stagegage.services.dto.response.ShowDto;
import com.stagegage.services.exception.InvalidFestivalDtoException;
import com.stagegage.services.repository.FestivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Scott on 7/11/14.
 *
 * @author Scott Hendrickson
 */
@Component
public class FestivalService {

    @Autowired
    private FestivalRepository festivalRepository;

    public List<FestivalDto> getFestivals(String name, String genre) {
        if(name != null || genre != null) {
            return festivalRepository.getFestivals(name, genre);
        } else {
            return festivalRepository.getAllFestivals();
        }
    }

    public FestivalDto createFestival(FestivalDto festivalDto) {
        if(festivalDto.isValid()) {
            return festivalRepository.createFestival(festivalDto);
        } else {
            throw new InvalidFestivalDtoException("Invalid Festival Dto");
        }
    }

    public FestivalDto getFestival(String festivalName) {
        return festivalRepository.getFestivalByName(festivalName);
    }

    public FestivalDto getFestivalShows(String festivalName) {
        return festivalRepository.getFestivalShows(festivalName);
    }

    public FestivalDto addFestivalShow(String festivalName, ShowDto showDto) {
        return null;
    }
}
