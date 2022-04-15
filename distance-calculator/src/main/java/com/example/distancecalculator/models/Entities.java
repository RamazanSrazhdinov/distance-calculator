package com.example.distancecalculator.models;

import com.example.distancecalculator.entities.CityEntity;
import com.example.distancecalculator.entities.DistanceEntity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "entities")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entities {
    @XmlElement(name = "city")
    private List<CityEntity> listCities = null;
    @XmlElement(name = "distance")
    private List<DistanceEntity> listDistances = null;

    public List<CityEntity> getListCities() {
        return listCities;
    }

    public List<DistanceEntity> getListDistances() {
        return listDistances;
    }

    public void setListCities(List<CityEntity> listCities) {
        this.listCities = listCities;
    }

    public void setListDistances(List<DistanceEntity> listDistances) {
        this.listDistances = listDistances;
    }
}
