package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Inventory {
    Map<String, FishingRod> userRods;
    Map<String, List<CapturedFish>> capturedFishes;

    public Inventory() {
        // Initialize maps to avoid NullPointerExceptions during operations
        this.userRods = new HashMap<>();
        this.capturedFishes = new HashMap<>();
    }

    public Inventory(Map<String ,FishingRod> userRods, HashMap<String, List<CapturedFish>> capturedFishes) {
        this.userRods = userRods;
        this.capturedFishes = capturedFishes;
    }

    public Map<String ,FishingRod> getUserRods() {
        return userRods;
    }

    public void setUserRods(Map<String, FishingRod> userRods) {
        this.userRods = userRods;
    }

    public Map<String, List<CapturedFish>> getCapturedFishes() {
        return capturedFishes;
    }

    public void setCapturedFishes(HashMap<String, List<CapturedFish>> capturedFishes) {
        this.capturedFishes = capturedFishes;
    }

    public void addCapturedFish(CapturedFish fish) {
        String speciesId = fish.getSpeciesFish().getId();
        this.capturedFishes.putIfAbsent(speciesId, new ArrayList<>());
        this.capturedFishes.get(speciesId).add(fish);
    }

    public void addFishingRod(FishingRod rod) {
        this.userRods.put(rod.getId(), rod);
    }

}
