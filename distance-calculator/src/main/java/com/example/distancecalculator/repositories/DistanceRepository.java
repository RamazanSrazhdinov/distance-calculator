package com.example.distancecalculator.repositories;

import com.example.distancecalculator.entities.DistanceEntity;
import org.springframework.data.repository.CrudRepository;

public interface DistanceRepository extends CrudRepository<DistanceEntity, Long> {
}
