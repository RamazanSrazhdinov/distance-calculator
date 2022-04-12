package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.City;

import java.util.List;

public interface CityService {
    List<City> getCities();
}
