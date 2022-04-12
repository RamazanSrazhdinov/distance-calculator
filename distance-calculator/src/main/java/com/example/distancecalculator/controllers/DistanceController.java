package com.example.distancecalculator.controllers;

import com.example.distancecalculator.dataParameters.DistanceData;
import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.exceptions.CityNotFoundException;
import com.example.distancecalculator.services.DistanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/distance")
public class DistanceController {
    private final DistanceService distanceService;

    @Autowired
    public DistanceController(DistanceService distanceService) {
        this.distanceService = distanceService;
    }

    @GetMapping()
    public ResponseEntity getDistance(@RequestBody DistanceData data){
        try {
            return ResponseEntity.ok(distanceService.getDistance(data.getType(), data.getFromCity(), data.getToCity()));
        }catch (CityNotFoundException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

}
