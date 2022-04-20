package com.example.distancecalculator.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "distance")
@XmlAccessorType(XmlAccessType.FIELD)
public class DistanceConverter {
    @XmlElement(name = "from_city")
    private String fromCity;
    @XmlElement(name = "to_city")
    private String toCity;
    @XmlElement(name = "distance")
    private Double distance;

    public DistanceConverter() {
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }
}
