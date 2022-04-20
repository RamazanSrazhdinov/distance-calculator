package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;
import com.example.distancecalculator.exceptions.TypeCalculateNotFound;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.DistanceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DistanceServiceImpl implements DistanceService {
    private final CityRepository cityRepository;
    private final DistanceRepository distanceRepository;
    private final BingMapsApiService bingMapsApiService;

    public DistanceServiceImpl(CityRepository cityRepository, DistanceRepository distanceRepository, BingMapsApiService bingMapsApiService) {
        this.cityRepository = cityRepository;
        this.distanceRepository = distanceRepository;
        this.bingMapsApiService = bingMapsApiService;
    }

    @Override
    public List<DistanceEntity> getDistance(String type, List<String> fromCity, List<String> toCity) throws CityNotFoundException, IOException, TypeCalculateNotFound {
        List<DistanceEntity> distanceEntityList = new ArrayList<>();

        if (checkIsCorrectType(type, "crowflight")) {
            calculateDistanceByCrowflight(fromCity, toCity, distanceEntityList);
        } else if (checkIsCorrectType(type, "distance_matrix")) {
            bingMapsApiService.calculateDistanceByDistanceMatrix(fromCity, toCity, distanceEntityList);
        } else if (checkIsCorrectType(type, "all")) {
            calculateDistanceByCrowflight(fromCity, toCity, distanceEntityList);
            bingMapsApiService.calculateDistanceByDistanceMatrix(fromCity, toCity, distanceEntityList);
        } else {
            throw new TypeCalculateNotFound("Тип расчета не найден");
        }
        return distanceEntityList;
    }

    private void calculateDistanceByCrowflight(List<String> fromCity, List<String> toCity, List<DistanceEntity> distanceEntityList) throws CityNotFoundException {
        CityEntity currentToCity;
        CityEntity currentFromCity;

        for (int i = 0; i < fromCity.size(); i++) {
            currentFromCity = cityRepository.getByName(fromCity.get(i));
            currentToCity = cityRepository.getByName(toCity.get(i));
            if (currentFromCity == null || currentToCity == null) {
                throw new CityNotFoundException("Город не найден");
            }
            checkIsCityAndCalculateDistance(distanceEntityList, currentFromCity, currentToCity);
        }
    }

    private void checkIsCityAndCalculateDistance(List<DistanceEntity> distanceEntityList, CityEntity currentFromCity, CityEntity currentToCity) throws CityNotFoundException {
        DistanceEntity distanceEntity;
        double distance = calculateTypeCrowflight(currentFromCity.getLatitude(), currentFromCity.getLongitude(), currentToCity.getLatitude(), currentToCity.getLongitude());
        distanceEntity = getDistanceEntity(currentFromCity, currentToCity, distance);
        distanceEntityList.add(distanceEntity);
    }

    private boolean checkIsCorrectType(String type, String argType) {
        return type.equalsIgnoreCase(argType);
    }

    private DistanceEntity getDistanceEntity(CityEntity currentFromCity, CityEntity currentToCity, double distance) {
        DistanceEntity distanceEntity = distanceRepository.findDistanceEntityByFromCityAndToCityAndDistance(currentFromCity, currentToCity, distance);
        if (distanceEntity == null) {
            distanceEntity = new DistanceEntity(currentFromCity, currentToCity, distance);
            distanceRepository.save(distanceEntity);
        }
        return distanceEntity;
    }

    private double calculateTypeCrowflight(double latA, double longA, double latB, double longB) {
        //радиус земли в метрах.
        double EARTH_RADIUS = 6372795;

        //Переводим угловые координаты в радианы
        double lat1 = latA * Math.PI / 180;
        double lat2 = latB * Math.PI / 180;
        double long1 = longA * Math.PI / 180;
        double long2 = longB * Math.PI / 180;

        //Получаем угловое расстояние в радианах, и так как в радианах просто умножаем на радиус.
        double a = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(long2 - long1));
        return Math.floor(Math.abs(EARTH_RADIUS * a / 1000));
    }
}