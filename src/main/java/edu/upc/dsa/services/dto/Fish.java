package edu.upc.dsa.services.dto;

public class Fish {

    private String speciesName;    // species name
    private int rarity;   // rarity level
    private double speciesWeight;  // standard weight of the species
    String url;

    public Fish() {
    }

    public Fish(edu.upc.dsa.models.Fish fish) {
        String speciesName = fish.getSpeciesName();
        int rarity = fish.getRarity();
        double speciesWeight = fish.getSpeciesWeight();

        this.speciesName = speciesName;
        this.rarity = rarity;
        this.speciesWeight = speciesWeight;
        this.url = "/img/fishes/" + fish.getSpeciesName().toLowerCase().replaceAll(" ", "_")+".png";
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public double getSpeciesWeight() {
        return speciesWeight;
    }

    public void setSpeciesWeight(double speciesWeight) {
        this.speciesWeight = speciesWeight;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
