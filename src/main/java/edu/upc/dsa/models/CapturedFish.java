package edu.upc.dsa.models;

import java.sql.Timestamp;

public class CapturedFish {
    private String id;  // uuid
    private Fish speciesFish;  // fish species information
    private double weight;
    private Timestamp captureTime;  // time of capture

    public CapturedFish() {
    }

    public CapturedFish(Fish speciesFish, double weight, Timestamp captureTime) {
        this.id = java.util.UUID.randomUUID().toString();
        this.speciesFish = speciesFish;
        this.weight = weight;
        this.captureTime = captureTime;
    }

    // Convenience: capture with current time
    public CapturedFish(Fish speciesFish, double weight) {
        this(speciesFish, weight, new java.sql.Timestamp(System.currentTimeMillis()));
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

    // Helper for API serialization
    public String getSpeciesId() {
        return this.speciesFish != null ? this.speciesFish.getId() : null;
    }
}
