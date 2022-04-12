package com.example.distancecalculator.models;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "city_entity")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public City() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
