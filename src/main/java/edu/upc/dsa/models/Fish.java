package edu.upc.dsa.models;

import static java.util.UUID.randomUUID;

public class Fish {
    String id;      // uuid
    String speciesName;    // species name
    int rarity;   // rarity level
    double speciesWeight;  // standard weight of the species

    public Fish() {
    }

    public Fish(String speciesName, int rarity, double speciesWeight) {
        this.id = randomUUID().toString();
        this.speciesName = speciesName;
        this.rarity = rarity;
        this.speciesWeight = speciesWeight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSpeciesWeight() {
        return speciesWeight;
    }

    public void setSpeciesWeight(double speciesWeight) {
        this.speciesWeight = speciesWeight;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public String getSpeciesName() {
        return speciesName;
    }

    public void setSpeciesName(String speciesName) {
        this.speciesName = speciesName;
    }

}
