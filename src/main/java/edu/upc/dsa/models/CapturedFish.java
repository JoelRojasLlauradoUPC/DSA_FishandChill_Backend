package edu.upc.dsa.models;

import java.sql.Timestamp;

public class CapturedFish {
    Fish speciesFish;  // fish species information
    double weight;
    Timestamp captureTime;  // time of capture

    public CapturedFish() {
    }

    public CapturedFish(Fish speciesFish, double weight, Timestamp captureTime) {
        this.speciesFish = speciesFish;
        this.weight = weight;
        this.captureTime = captureTime;
    }

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

    @Override
    public String toString() {
        return "CapturedFish{" +
                "speciesFish=" + speciesFish.getSpeciesName() +
                ", weight=" + weight +
                ", captureTime=" + captureTime +
                '}';
    }
}
