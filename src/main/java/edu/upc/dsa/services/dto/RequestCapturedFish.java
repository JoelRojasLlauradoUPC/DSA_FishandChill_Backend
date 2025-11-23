package edu.upc.dsa.services.dto;

public class RequestCapturedFish {
    private String speciesName;
    private double weight;

    public RequestCapturedFish() {
    }

    public RequestCapturedFish(String speciesName, double weight) {
        this.speciesName = speciesName;
        this.weight = weight;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
