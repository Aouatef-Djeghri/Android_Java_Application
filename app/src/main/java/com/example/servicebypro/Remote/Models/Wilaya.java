package com.example.servicebypro.Remote.Models;

import java.util.ArrayList;
import java.util.List;

public class Wilaya {


    private String name;
    private int number;
    private float latitude;
    private float longitude;
    List<String> communes = new ArrayList<>();

    public Wilaya() {
    }

    public Wilaya(String name, int number, float latitude, float longitude, List<String> communes) {
        this.name = name;
        this.number = number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.communes = communes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public void setCommunes(List<String> communes) {
        this.communes = communes;
    }

    public int getNumber() {
        return number;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public List<String> getCommunes() {
        return communes;
    }
}
