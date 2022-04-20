package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.DistanceRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class BingMapsApiService {
    private final RestTemplate restTemplate;
    private final CityRepository cityRepository;
    private final DistanceRepository distanceRepository;

    @Autowired
    public BingMapsApiService(RestTemplateBuilder restTemplate, CityRepository cityRepository, DistanceRepository distanceRepository) {
        this.restTemplate = restTemplate.build();
        this.cityRepository = cityRepository;
        this.distanceRepository = distanceRepository;
    }

    public void calculateDistanceByDistanceMatrix(List<String> fromCity, List<String> toCity, List<DistanceEntity> distanceEntityList) throws JsonProcessingException, CityNotFoundException {
        DistanceEntity distanceEntity = null;
        CityEntity toCityEntity = null;
        CityEntity fromCityEntity = null;

        for (int i = 0; i < fromCity.size(); i++) {
            fromCityEntity = cityRepository.findByName(fromCity.get(i));
            toCityEntity = cityRepository.findByName(toCity.get(i));

            String json = requestForGettingDistanceByCitiesName(fromCity, toCity, i);
            JsonNode distanceNode = getJsonNode(json);

            if (isStatusCodeOk(distanceNode) && fromCityEntity != null && toCityEntity != null) {
                Double distanceValue = distanceNode.get("resourceSets").get(0).get("resources").get(0).get("travelDistance").asDouble();
                distanceEntity = distanceRepository.findDistanceEntityByFromCityAndToCityAndDistance(fromCityEntity, toCityEntity, distanceValue);

                if (distanceEntity == null) {
                    distanceEntity = new DistanceEntity(fromCityEntity, toCityEntity, distanceValue);
                    distanceRepository.save(distanceEntity);
                }
                distanceEntityList.add(distanceEntity);
            } else {
                throw new CityNotFoundException("Город не найден");
            }
        }
    }

    private boolean isStatusCodeOk(JsonNode distanceNode) {
        return distanceNode.get("statusCode").intValue() == 200;
    }

    private JsonNode getJsonNode(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(json);
    }

    private String requestForGettingDistanceByCitiesName(List<String> fromCity, List<String> toCity, int i) {
        String url = String.format("http://dev.virtualearth.net/REST/V1/Routes/Driving?wp.0=%s&wp.1=%s&routeAttributes=excludeItinerary&key=AjAL4-tRsqOtwwXUEZ6sYqsN4wYuDlbqnqAngnfpCfscp21qO2MreqSAm0qo0EzB", fromCity.get(i), toCity.get(i));
        return restTemplate.getForObject(url, String.class);
    }
}
