package edu.upc.dsa.models;

import java.util.*;

public class Inventory {
    private Map<String, FishingRod> userRods;
    private Map<String, List<CapturedFish>> capturedFishes;
    private String equippedRodId; // nullable

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
        return Collections.unmodifiableMap(userRods);
    }

    public void setUserRods(Map<String, FishingRod> userRods) {
        this.userRods = userRods;
    }

    public Map<String, List<CapturedFish>> getCapturedFishes() {
        return Collections.unmodifiableMap(capturedFishes);
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
        if (this.equippedRodId == null) this.equippedRodId = rod.getId();
    }

    // Helpers for game logic
    public boolean hasRod(String rodId) {
        return this.userRods.containsKey(rodId);
    }

    public int getTotalCapturesForSpecies(String speciesId) {
        List<CapturedFish> list = this.capturedFishes.get(speciesId);
        return list == null ? 0 : list.size();
    }

    public boolean equipRod(String rodId) {
        if (!this.userRods.containsKey(rodId)) return false;
        this.equippedRodId = rodId;
        return true;
    }

    public String getEquippedRodId() {
        return this.equippedRodId;
    }
}
