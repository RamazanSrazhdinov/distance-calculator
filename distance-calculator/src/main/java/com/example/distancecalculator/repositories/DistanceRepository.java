package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DistanceRepository extends JpaRepository<DistanceEntity, Long> {
    DistanceEntity findDistanceEntityByFromCityAndToCityAndDistance(CityEntity fromCity, CityEntity toCity, Double distance);
    Double getDistanceByFromCityAndToCity(CityEntity fromCity, CityEntity toCity);
}
