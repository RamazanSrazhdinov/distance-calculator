package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;
import com.example.distancecalculator.exceptions.TypeCalculateNotFound;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface DistanceService {
    List<DistanceEntity> getDistance(String type, List<String> fromCity, List<String> toCity) throws CityNotFoundException, IOException, TypeCalculateNotFound;
}
