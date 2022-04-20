package com.example.distancecalculator.services;

import com.example.distancecalculator.dto.CityDTO;
import com.example.distancecalculator.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public List<CityDTO> getCities() {
        return cityRepository.findAll().stream().map(city -> {
            CityDTO cityDTO = new CityDTO();
            cityDTO.setId(city.getId());
            cityDTO.setName(city.getName());
            return cityDTO;
        }).collect(Collectors.toList());
    }
}
