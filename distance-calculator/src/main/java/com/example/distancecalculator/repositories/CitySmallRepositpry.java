package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CitySmallRepositpry extends JpaRepository<City, Long> {
}
