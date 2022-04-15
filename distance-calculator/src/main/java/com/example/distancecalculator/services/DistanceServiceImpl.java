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
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class DistanceServiceImpl implements DistanceService {
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;
    private final DistanceRepository distanceRepository;

    @Autowired
    public DistanceServiceImpl(RestTemplateBuilder restTemplate, CityRepository cityRepository, DistanceRepository distanceRepository) {
        this.restTemplate = restTemplate.build();
        this.cityRepository = cityRepository;
        this.distanceRepository = distanceRepository;
    }

    @Override
    public List<DistanceEntity> getDistance(String type, List<String> fromCity, List<String> toCity) throws CityNotFoundException, IOException, TypeCalculateNotFound {
        List<DistanceEntity> distanceEntityList = new ArrayList<>();

        if (checkIsCorrectType(type, "crowflight")) {
            calculateDistanceByCrowflight(fromCity, toCity, distanceEntityList);
        } else if (checkIsCorrectType(type, "distance_matrix")) {
            calculateDistanceByDistanceMatrix(fromCity, toCity, distanceEntityList);
        } else if (checkIsCorrectType(type, "all")) {
            calculateDistanceByCrowflight(fromCity, toCity, distanceEntityList);
            calculateDistanceByDistanceMatrix(fromCity, toCity, distanceEntityList);
        } else {
            throw new TypeCalculateNotFound("Тип расчета не найден");
        }
        return distanceEntityList;
    }

    private void calculateDistanceByDistanceMatrix(List<String> fromCity, List<String> toCity, List<DistanceEntity> distanceEntityList) throws JsonProcessingException, CityNotFoundException {
        DistanceEntity distance = null;
        for (int i = 0; i < fromCity.size(); i++) {
            String json = getJson(fromCity, toCity, i);
            JsonNode rootNode = getJsonNode(json);

            if (isStatusCodeOk(rootNode)) {
                Double distanceValue = rootNode.get("resourceSets").get(0).get("resources").get(0).get("travelDistance").asDouble();
                DistanceEntity currentDistanceEntity = distanceRepository.findByFromCityAndToCityAndDistance(fromCity.get(i), toCity.get(i), distanceValue);
                if (currentDistanceEntity == null) {
                    distance = new DistanceEntity(fromCity.get(i), toCity.get(i), distanceValue);
                    distanceRepository.save(distance);
                } else {
                    distance = currentDistanceEntity;
                }
                distanceEntityList.add(distance);
            } else {
                throw new CityNotFoundException("Город не найден");
            }
        }
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

    private boolean isStatusCodeOk(JsonNode rootNode) {
        return rootNode.get("statusCode").intValue() == 200;
    }

    private JsonNode getJsonNode(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    private String getJson(List<String> fromCity, List<String> toCity, int i) {
        String url = String.format("http://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=%s&wp.1=%s&routeAttributes=excludeItinerary&key=AjAL4-tRsqOtwwXUEZ6sYqsN4wYuDlbqnqAngnfpCfscp21qO2MreqSAm0qo0EzB", fromCity.get(i), toCity.get(i));
        return restTemplate.getForObject(url, String.class);
    }

    private void checkIsCityAndCalculateDistance(List<DistanceEntity> distanceEntityList, CityEntity currentFromCity, CityEntity currentToCity) {
        DistanceEntity distanceEntity;
        double distance = calculateTypeCrowflight(currentFromCity.getLatitude(), currentFromCity.getLongitude(), currentToCity.getLatitude(), currentToCity.getLongitude());
        distanceEntity = getDistanceEntity(currentFromCity, currentToCity, distance);
        distanceEntityList.add(distanceEntity);
    }

    private boolean checkIsCorrectType(String type, String argType) {
        return type.equalsIgnoreCase(argType);
    }

    private DistanceEntity getDistanceEntity(CityEntity currentFromCity, CityEntity currentToCity, double distance) {
        DistanceEntity distanceEntity = null;
        DistanceEntity currentDistance = distanceRepository.findByFromCityAndToCityAndDistance(currentFromCity.getName(), currentToCity.getName(), distance);
        if (currentDistance == null) {
            distanceEntity = new DistanceEntity(currentFromCity.getName(), currentToCity.getName(), distance);
            distanceRepository.save(distanceEntity);
        } else {
            distanceEntity = distanceRepository.findByFromCityAndToCityAndDistance(currentFromCity.getName(), currentToCity.getName(), distance);
        }
        return distanceEntity;
    }

    private boolean isCityEntityNotNull(CityEntity currentFromCity, CityEntity currentToCity) {
        return currentFromCity != null && currentToCity != null;
    }

    private boolean checkIsNotNull(CityEntity fromCity, CityEntity toCity) {
        return fromCity == null && toCity == null;
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
