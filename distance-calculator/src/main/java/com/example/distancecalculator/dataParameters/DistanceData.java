package com.example.distancecalculator.dataParameters;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DistanceData {
    private String type;
    private List<String> from_city;
    private List<String> to_city;

    public DistanceData(String type, List<String> from_city, List<String> to_city) {
        this.type = type;
        this.from_city = from_city;
        this.to_city = to_city;
    }

    public String getType() {
        return type;
    }

    public List<String> getFromCity() {
        return from_city;
    }

    public List<String> getToCity() {
        return to_city;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFrom_city(List<String> from_city) {
        this.from_city = from_city;
    }

    public void setTo_city(List<String> to_city) {
        this.to_city = to_city;
    }
}
