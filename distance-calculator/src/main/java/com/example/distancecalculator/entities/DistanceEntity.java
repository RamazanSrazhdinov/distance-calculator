package com.example.distancecalculator.entities;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "distance")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "distance")
public class DistanceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @XmlElement(name = "from_city")
    @ManyToOne
    @JoinColumn(name = "from_city")
    private CityEntity fromCity;

    @XmlElement(name = "to_city")
    @ManyToOne
    @JoinColumn(name = "to_city")
    private CityEntity toCity;

    @XmlElement(name = "distance")
    private Double distance;

    public DistanceEntity() {
    }


}
