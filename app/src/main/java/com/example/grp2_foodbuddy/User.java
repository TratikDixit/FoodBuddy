package com.example.grp2_foodbuddy;

import java.util.List;

public class User {
    private String name;
    private float rating;
    List<Groups> currentGroups;

    public User(String name, float rating, List<Groups> currentGroups){
        this.name = name;
        this.rating = rating;
        this.currentGroups = currentGroups;
    }

    public List<Groups> getCurrentGroups() {
        return currentGroups;
    }

    public void setCurrentGroups(List<Groups> currentGroups) {
        this.currentGroups = currentGroups;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }
}
