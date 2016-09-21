package com.les4elefantastiq.comeeting.models;

import java.util.List;

public class Coworkspace {

    // -------------- Objects, Variables -------------- //

    public String id;
    public String name;
    public String pictureUrl;
    public String description;
    public Double geolocationLongitude; // TODO: Create a Location object ?
    public Double geolocationLatitude;
    public int geofancingRadius;
    public String address;
    public String zipCode;
    public String city;
    public List<Coworker> coworkers;


    // ----------------- Constructor ------------------ //

    public Coworkspace(String id, String name, String pictureUrl, String description, Double geolocationLatitude, Double geolocationLongitude, int geofancingRadius, String address, String zipCode, String city, List<Coworker> coworkers) {
        this.id = id;
        this.name = name;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.geolocationLatitude = geolocationLatitude;
        this.geolocationLongitude = geolocationLongitude;
        this.geofancingRadius = geofancingRadius;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.coworkers = coworkers;
    }

}