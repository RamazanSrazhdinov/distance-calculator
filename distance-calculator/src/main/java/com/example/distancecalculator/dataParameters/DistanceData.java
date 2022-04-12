package com.example.distancecalculator.dataParameters;

import java.util.List;

public class DistanceData {
    private String type;
    private List<String> fromCity;
    private List<String> toCity;

    public DistanceData(String type, List<String> fromCity, List<String> toCity) {
        this.type = type;
        this.fromCity = fromCity;
        this.toCity = toCity;
    }

    public String getType() {
        return type;
    }

    public List<String> getFromCity() {
        return fromCity;
    }

    public List<String> getToCity() {
        return toCity;
    }
}
