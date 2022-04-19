package com.example.distancecalculator.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.*;
import java.util.List;

@XmlRootElement(name = "city")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "city")
public class CityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "latitude")
    private Double latitude;
    @XmlElement(name = "longitude")
    private Double longitude;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fromCity")
    private List<DistanceEntity> distancesFromCity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "toCity")
    private List<DistanceEntity> distancesToCity;

    public CityEntity() {
    }

    public CityEntity(String name, Double latitude, Double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
