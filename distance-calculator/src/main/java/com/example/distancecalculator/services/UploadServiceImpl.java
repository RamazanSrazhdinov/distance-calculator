package com.example.distancecalculator.services;

import com.example.distancecalculator.models.DistanceConverter;
import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.models.Entities;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.DistanceRepository;
import liquibase.pro.packaged.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.ArrayList;

@Service
public class UploadServiceImpl implements UploadService {
    private DistanceRepository distanceRepository;
    private CityRepository cityRepository;

    @Autowired
    public UploadServiceImpl(DistanceRepository distanceRepository, CityRepository cityRepository) {
        this.distanceRepository = distanceRepository;
        this.cityRepository = cityRepository;
    }

    public void uploadAll(MultipartFile file) throws IOException, JAXBException {
        Entities entities = new Entities();
        entities.setListCities(new ArrayList<CityEntity>());
        entities.setListDistances(new ArrayList<DistanceConverter>());

        Entities listEntities = (Entities) getUnmarshaller(Entities.class).unmarshal(file.getInputStream());

        if (listEntities.getListCities() != null) {
            listEntities.getListCities().stream().filter(this::cityCheckIsEmpty)
                    .forEach(cityEntity -> cityRepository.save(cityEntity));
        }

        if (listEntities.getListDistances() != null) {
            listEntities.getListDistances().stream().map(distanceXml -> new DistanceEntity(
                            cityRepository.getByName(distanceXml.getFromCity()),
                            cityRepository.getByName(distanceXml.getToCity()),
                            distanceXml.getDistance()
                    )).filter(distanceEntity -> !cityCheckIsEmpty(distanceEntity.getFromCity()) && !cityCheckIsEmpty(distanceEntity.getToCity()))
                    .filter(this::isDistanceNotNull)
                    .forEach(distanceEntity -> {
                        distanceRepository.save(distanceEntity);
                    });
        }
    }

    @Override
    public void uploadCity(MultipartFile files) throws IOException, JAXBException {
        CityEntity cityEntity = (CityEntity) getUnmarshaller(CityEntity.class).unmarshal(files.getInputStream());
        if (cityCheckIsEmpty(cityEntity)) {
            cityRepository.save(cityEntity);
        }
    }

    @Override
    public void uploadDistance(MultipartFile files) throws IOException, JAXBException {
        DistanceConverter distanceConverter = (DistanceConverter) getUnmarshaller(DistanceConverter.class).unmarshal(files.getInputStream());
        if (isFromCityAndToCityNotEmpty(distanceConverter)) {
            DistanceEntity distance = new DistanceEntity(
                    cityRepository.getByName(distanceConverter.getFromCity()),
                    cityRepository.getByName(distanceConverter.getToCity()),
                    distanceConverter.getDistance()
            );
            if (isDistanceNotNull(distance)) {
                distanceRepository.save(distance);
            }
        }

    }

    private boolean isDistanceNotNull(DistanceEntity distanceEntity) {
        return distanceRepository.findDistanceEntityByFromCityAndToCityAndDistance
                (distanceEntity.getFromCity(), distanceEntity.getToCity(), distanceEntity.getDistance()) == null;
    }

    private boolean isFromCityAndToCityNotEmpty(DistanceConverter distanceConverter) {
        return cityRepository.findByName(distanceConverter.getFromCity()) != null && cityRepository.findByName(distanceConverter.getToCity()) != null;
    }

    private boolean cityCheckIsEmpty(CityEntity cityEntity) {
        return cityRepository.getByName(cityEntity.getName()) == null;
    }

    private Unmarshaller getUnmarshaller(Class<?> entity) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(entity);
        return context.createUnmarshaller();
    }
}