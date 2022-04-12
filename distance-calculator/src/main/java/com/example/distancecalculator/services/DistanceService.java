package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;

import java.util.List;

public interface DistanceService {
    List<DistanceEntity> getDistance(String type, List<String> fromCity, List<String> toCity) throws CityNotFoundException;
}
