package edu.upc.dsa.services.dto;

import java.sql.Timestamp;

public class SellCapturedFish {
    private String fishSpeciesName;
    private float weight;
    private Timestamp captureTime;
    private int price;

    public SellCapturedFish() {
    }

    public SellCapturedFish(String fishSpeciesName, float weight, Timestamp captureTime, int price) {
        this.fishSpeciesName = fishSpeciesName;
        this.weight = weight;
        this.captureTime = captureTime;
        this.price = price;
    }

    public String getFishSpeciesName() {
        return fishSpeciesName;
    }

    public void setFishSpeciesName(String fishSpeciesName) {
        this.fishSpeciesName = fishSpeciesName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Timestamp getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Timestamp captureTime) {
        this.captureTime = captureTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
