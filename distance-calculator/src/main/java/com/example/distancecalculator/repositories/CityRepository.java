package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.CityEntity;
import org.springframework.data.repository.CrudRepository;

public interface CityRepository extends CrudRepository<CityEntity, Long> {
}
