package com.les4elefantastiq.comeeting.models;

import java.util.List;

/**
 * Representation of a place where Coworkers go to work
 */
public class Coworkspace {

    // -------------- Objects, Variables -------------- //

    public final String id;
    public final String name;
    public final String pictureUrl;
    public final String description;
    public final Double geolocationLongitude;
    public final Double geolocationLatitude;
    public final int geofancingRadius;
    public final String address;
    public final String zipCode;
    public final String city;
    public final List<Coworker> coworkers;


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