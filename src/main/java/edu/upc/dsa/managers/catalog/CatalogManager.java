package edu.upc.dsa.managers.catalog;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CatalogManager {
    private final Map<String, Fish> fish = new ConcurrentHashMap<>();
    private final Map<String, FishingRod> rods = new ConcurrentHashMap<>();

    public int addFish(Fish f) {
        if (fish.containsKey(f.getId())) return -1;
        fish.put(f.getId(), f);
        return 1;
    }
    public int addRod(FishingRod r) {
        if (rods.containsKey(r.getId())) return -1;
        rods.put(r.getId(), r);
        return 1;
    }
    public Map<String, Fish> getFishMap() { return fish; }
    public Map<String, FishingRod> getRodsMap() { return rods; }
    public Collection<Fish> listFish() { return fish.values(); }
    public Collection<FishingRod> listRods() { return rods.values(); }
}

