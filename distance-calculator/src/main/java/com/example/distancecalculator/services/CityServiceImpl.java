package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.City;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.CitySmallRepositpry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CityServiceImpl implements CityService {
    private final CitySmallRepositpry citySmallRepositpry;
    private final CityRepository cityRepository;

    @Autowired
    public CityServiceImpl(CitySmallRepositpry citySmallRepositpry, CityRepository cityRepository) {
        this.citySmallRepositpry = citySmallRepositpry;
        this.cityRepository = cityRepository;
    }

    @Override
    public List<City> getCities() {
        return citySmallRepositpry.findAll();
    }
}
