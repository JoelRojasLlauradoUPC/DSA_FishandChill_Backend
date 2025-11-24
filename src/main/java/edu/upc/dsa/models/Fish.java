package edu.upc.dsa.models;

import java.util.Objects;

import static java.util.UUID.randomUUID;

public class Fish {
    private int id;      // uuid
    private String speciesName;    // species name
    private int rarity;   // rarity level
    private double speciesWeight;  // standard weight of the species

    public Fish() {
    }

    public Fish(String speciesName, int rarity, double speciesWeight) {
        this.id = 0;
        this.speciesName = speciesName;
        this.rarity = rarity;
        this.speciesWeight = speciesWeight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fish fish = (Fish) o;
        return Objects.equals(id, fish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Fish{" +
                "id='" + id + '\'' +
                ", speciesName='" + speciesName + '\'' +
                ", rarity=" + rarity +
                ", speciesWeight=" + speciesWeight +
                '}';
    }
}
