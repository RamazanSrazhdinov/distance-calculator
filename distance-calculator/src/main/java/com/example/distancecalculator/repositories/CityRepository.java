package com.example.distancecalculator.repositories;
import com.example.distancecalculator.entities.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
    CityEntity getByName(String name);
}