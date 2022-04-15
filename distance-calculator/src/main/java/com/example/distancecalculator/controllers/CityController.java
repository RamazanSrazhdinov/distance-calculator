package com.example.distancecalculator.controllers;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.models.CityModel;
import com.example.distancecalculator.services.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
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
    public ResponseEntity getCities() throws JAXBException {
        List<CityModel> cities = cityService.getCities();
        return ResponseEntity.ok(cities);
    }
}
