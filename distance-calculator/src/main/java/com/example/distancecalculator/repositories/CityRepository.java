package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

}
