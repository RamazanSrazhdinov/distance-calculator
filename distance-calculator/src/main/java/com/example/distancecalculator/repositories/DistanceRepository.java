package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.DistanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface DistanceRepository extends JpaRepository<DistanceEntity, Long> {
    DistanceEntity findByFromCityAndToCityAndDistance(String fromCity, String toCity, Double distance);
}
