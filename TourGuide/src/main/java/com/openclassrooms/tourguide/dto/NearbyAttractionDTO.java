package com.openclassrooms.tourguide.dto;

public class NearbyAttractionDTO {
    public String attractionName;
    public double attractionLatitude;
    public double attractionLongitude;
    public double distanceInMiles;
    public int rewardPoints;
    public double userLatitude;
    public double userLongitude;

    public NearbyAttractionDTO(String attractionName, double attractionLatitude, double attractionLongitude,
                               double distanceInMiles, int rewardPoints, double userLatitude, double userLongitude) {
        this.attractionName = attractionName;
        this.attractionLatitude = attractionLatitude;
        this.attractionLongitude = attractionLongitude;
        this.distanceInMiles = distanceInMiles;
        this.rewardPoints = rewardPoints;
        this.userLatitude = userLatitude;
        this.userLongitude = userLongitude;
    }
}