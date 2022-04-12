package com.example.distancecalculator.repositories;

import com.example.distancecalculator.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityModelRepository extends JpaRepository<City, Long> {
}
