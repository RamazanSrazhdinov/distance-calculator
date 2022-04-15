package com.example.distancecalculator.repositories;

import com.example.distancecalculator.models.CityModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityModelRepository extends JpaRepository<CityModel, Long> {
}
