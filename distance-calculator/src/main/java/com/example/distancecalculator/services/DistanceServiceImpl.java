package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.DistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DistanceServiceImpl implements DistanceService {
    private final CityRepository cityRepository;
    private final DistanceRepository distanceRepository;

    @Autowired
    public DistanceServiceImpl(CityRepository cityRepository, DistanceRepository distanceRepository) {
        this.cityRepository = cityRepository;
        this.distanceRepository = distanceRepository;
    }


    @Override
    public List<DistanceEntity> getDistance(String type, List<String> fromCity, List<String> toCity) throws CityNotFoundException {
        List<DistanceEntity> distanceEntityList = new ArrayList<>();
        CityEntity currentFromCity = null;
        CityEntity currentToCity = null;

        if (type.equalsIgnoreCase("crowflight")) {
            for (int i = 0; i < fromCity.size(); i++) {
                currentFromCity = cityRepository.getByName(fromCity.get(i));
                currentToCity = cityRepository.getByName(toCity.get(i));

                if (checkIsNotNull(currentFromCity, currentToCity)) {
                    double distance = calculateTypeCrowflight(currentFromCity.getLatitude(), currentFromCity.getLongitude(), currentToCity.getLatitude(), currentToCity.getLongitude());
                    DistanceEntity distanceEntity = new DistanceEntity(currentFromCity.getName(), currentToCity.getName(), distance);
                    distanceEntityList.add(distanceEntity);
                    distanceRepository.save(distanceEntity);
                } else {
                    throw new CityNotFoundException("Город не найден");
                }
            }
        }

        return distanceEntityList;

    }

   /* private boolean checkIsCity( CityEntity fromCity, String fromCityName, CityEntity toCity, String toCityName){
        if (fromCity.getName().isEmpty() && toCity.getName().isEmpty()){
            if ((!fromCity.getName().equalsIgnoreCase(fromCityName) && toCity.getName().equalsIgnoreCase(toCityName))){
                return true;
            }
        }
        return false;
    }*/

    private boolean checkIsNotNull(CityEntity fromCity, CityEntity toCity){
        return fromCity != null && toCity != null;
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
