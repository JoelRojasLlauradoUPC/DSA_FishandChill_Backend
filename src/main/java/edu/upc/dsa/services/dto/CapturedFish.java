package edu.upc.dsa.services.dto;

import java.sql.Time;
import java.sql.Timestamp;

public class CapturedFish {
    private Fish fishSpecies;
    private double weight;
    private Timestamp captureTime;

    public CapturedFish() {
    }

    public CapturedFish(edu.upc.dsa.models.Fish fishSpecies, double weight, Timestamp captureTime) {
        this.fishSpecies = new Fish(fishSpecies);
        this.weight = weight;
        this.captureTime = captureTime;
    }

    public Fish getFishSpecies() {
        return fishSpecies;
    }

    public void setFishSpecies(Fish fishSpecies) {
        this.fishSpecies = fishSpecies;
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
