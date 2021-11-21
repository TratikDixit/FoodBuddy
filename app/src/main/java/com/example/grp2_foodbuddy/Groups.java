package com.example.grp2_foodbuddy;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

public class Groups {

    private int duration;
    private User initiator;
    private int maxParticipants;
    private List<User> participants;
    private double threshold_rating;
    private GeoPoint pickup_location;
    private int numParticipants;

    public Groups() {
    }

    public Groups(int duration,double rating, User initiator, int maxParticipants, List<User> participants, GeoPoint geopoint) {
        this.duration = duration;
        this.initiator = initiator;
        this.maxParticipants = maxParticipants;
        this.participants = participants;
        this.pickup_location = geopoint;
        this.threshold_rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public User getInitiator() {
        return initiator;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public double getThreshold_rating() {
        return threshold_rating;
    }

    public GeoPoint getPickup_location() {
        return pickup_location;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void setThreshold_rating(double threshold_rating) {
        this.threshold_rating = threshold_rating;
    }

    public void setPickup_location(GeoPoint pickup_location) {
        this.pickup_location = pickup_location;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
    }
}
