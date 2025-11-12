package edu.upc.dsa.models;

import java.sql.Timestamp;

public class CapturedFish {
    String id;  // uuid
    Fish speciesFish;  // fish species information
    double weight;
    Timestamp captureTime;  // time of capture

    public CapturedFish() {
    }

    public CapturedFish(Fish speciesFish, double weight, Timestamp captureTime) {
        this.id = java.util.UUID.randomUUID().toString();
        this.speciesFish = speciesFish;
        this.weight = weight;
        this.captureTime = captureTime;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public Fish getSpeciesFish() {
        return speciesFish;
    }

    public void setSpeciesFish(Fish speciesFish) {
        this.speciesFish = speciesFish;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Timestamp getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Timestamp captureTime) {
        this.captureTime = captureTime;
    }

}
