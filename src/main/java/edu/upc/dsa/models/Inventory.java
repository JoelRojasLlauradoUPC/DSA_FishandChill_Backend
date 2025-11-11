package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    String id;
    List<FishingRod> availableRods;
    List<Fish> availableFish;

    public Inventory() {
        this.availableRods = new ArrayList<>();
        this.availableFish = new ArrayList<>();
    }

    public Inventory(String id) {
        this();
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<FishingRod> getAvailableRods() {
        return availableRods;
    }

    public void setAvailableRods(List<FishingRod> availableRods) {
        this.availableRods = availableRods;
    }

    public List<Fish> getAvailableFish() {
        return availableFish;
    }

    public void setAvailableFish(List<Fish> availableFish) {
        this.availableFish = availableFish;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id='" + id + '\'' +
                ", availableRods=" + availableRods +
                ", availableFish=" + availableFish +
                '}';
    }
}
