package com.example.grp2_foodbuddy;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.List;

public class Restaurant {


    private String name;
    private String imageID;
    private ArrayList<Groups> ongoing;
    private GeoPoint geopoint;

    public Restaurant(String name, String Id, GeoPoint geoPoint){
        this.name = name;
        this.imageID = Id;
        this.geopoint = geoPoint;
    }

    public Restaurant() {}

    public void setGeopoint(GeoPoint geopoint) {
        this.geopoint = geopoint;
    }

    public GeoPoint getGeopoint() {
        return geopoint;
    }

    public String getName() {
        return name;
    }

    public String getImageID() {
        return imageID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageID(String imageID) {
        this.imageID = imageID;
    }

    public List<Groups> getOngoing() {
        return ongoing;
    }

    public void setOngoing(ArrayList<Groups> ongoing) {
        this.ongoing = ongoing;
    }
}
