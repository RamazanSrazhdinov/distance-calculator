package com.example.distancecalculator.controllers;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.City;
import com.example.distancecalculator.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping
    public ResponseEntity getCities(){
        List<City> cities = cityService.getCities();
        return ResponseEntity.ok(cities);
    }
}
