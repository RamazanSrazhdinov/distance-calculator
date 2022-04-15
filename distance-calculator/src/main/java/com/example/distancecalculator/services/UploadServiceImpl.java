package com.example.distancecalculator.services;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;
import com.example.distancecalculator.models.Entities;
import com.example.distancecalculator.repositories.CityRepository;
import com.example.distancecalculator.repositories.DistanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {
    private final static String PATH = "xmlDatabases/";
    private DistanceRepository distanceRepository;
    private CityRepository cityRepository;

    @Autowired
    public UploadServiceImpl(DistanceRepository distanceRepository, CityRepository cityRepository) {
        this.distanceRepository = distanceRepository;
        this.cityRepository = cityRepository;
    }

    public void uploadAll(MultipartFile file) throws IOException, JAXBException {
        String name = generateXmlFileName();
        uploadFileInDirectory(file.getInputStream(), PATH, name);
        CityEntity city = null;
        DistanceEntity distance = null;

        Entities entities = new Entities();
        entities.setListCities(new ArrayList<CityEntity>());
        entities.setListDistances(new ArrayList<DistanceEntity>());

        JAXBContext context = JAXBContext.newInstance(Entities.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Entities listEntities = (Entities) unmarshaller.unmarshal(new File(PATH + name));

        if (listEntities.getListCities() != null) {
            for (CityEntity cities : listEntities.getListCities()) {
                if (cityRepository.getByName(cities.getName()) == null) {
                    city = new CityEntity(cities.getName(), cities.getLatitude(), cities.getLatitude());
                    cityRepository.save(city);
                }
            }
        }

        if (listEntities.getListDistances() != null) {
            for (DistanceEntity distanceEntity : listEntities.getListDistances()) {
                if (distanceRepository.findByFromCityAndToCityAndDistance(distanceEntity.getFromCity(), distanceEntity.getToCity(), distanceEntity.getDistance()) == null) {
                    distance = new DistanceEntity(distanceEntity.getFromCity(), distanceEntity.getToCity(), distanceEntity.getDistance());
                    distanceRepository.save(distance);
                }
            }
        }
    }

    public void uploadCity(MultipartFile files) throws IOException, JAXBException {
        String name = generateXmlFileName();
        uploadFileInDirectory(files.getInputStream(), PATH, name);
        saveCityEntity(PATH, name);
    }

    public void uploadDistance(MultipartFile files) throws IOException, JAXBException {
        String name = generateXmlFileName();
        uploadFileInDirectory(files.getInputStream(), PATH, name);
        saveDistanceEntity(PATH, name);
    }


    private String generateXmlFileName() {
        return UUID.randomUUID().toString() + ".xml";
    }

    private void saveDistanceEntity(String path, String name) throws JAXBException {
        DistanceEntity distanceEntity = (DistanceEntity) getUnmarshaller(DistanceEntity.class).unmarshal(new File(path + name));
        System.out.println(distanceEntity.getDistance()+distanceEntity.getToCity()+distanceEntity.getFromCity());
        if (distanceRepository.findByFromCityAndToCityAndDistance(distanceEntity.getFromCity(), distanceEntity.getToCity(), distanceEntity.getDistance()) == null) {
            distanceRepository.save(distanceEntity);
        }
    }

    private void saveCityEntity(String path, String name) throws JAXBException {
        CityEntity cityEntity = (CityEntity) getUnmarshaller(CityEntity.class).unmarshal(new File(path + name));
        if (cityRepository.findByName(cityEntity.getName()) == null) {
            cityRepository.save(cityEntity);
        }
    }

    private void uploadFileInDirectory(InputStream stream, String path, String fileName) throws IOException {
        Files.copy(stream, Path.of(path + fileName));
    }

    private Unmarshaller getUnmarshaller(Class<?> entity) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(entity);
        return context.createUnmarshaller();
    }

    private Document getXmlDocument(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        return docBuilder.parse(new File("xmlDatabases/" + fileName));
    }

}
