package edu.upc.dsa.managers.catalog;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.orm.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CatalogManager {
//    private final Map<String, Fish> fish = new ConcurrentHashMap<>();
//    private final Map<String, FishingRod> rods = new ConcurrentHashMap<>();

    //FISHES
    public Fish getFish(String fishSpeciesName) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("speciesName", fishSpeciesName);
        List<Object> result = session.get(Fish.class, params);
        session.close();
        if (result.isEmpty()) return null;
        Fish fish = (Fish) result.get(0);
        return fish;
    }

//    public Fish getFish(int fishId) {
//        Session session = FactorySession.openSession();
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("id", fishId);
//        List<Object> result = session.get(Fish.class, params);
//        session.close();
//        if (result.isEmpty()) return null;
//        Fish fish = (Fish) result.get(0);
//        return fish;
//    }

    public List<Fish> getAllFishes() {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(Fish.class, params);
        session.close();
        List<Fish> allFishes = (List<Fish>)(List<?>) result;
        return allFishes;
    }

    //FISHING RODS
    public FishingRod getFishingRod(String fishingRodName) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", fishingRodName);
        List<Object> result = session.get(Fish.class, params);
        session.close();
        if (result.isEmpty()) return null;
        FishingRod fishingRod = (FishingRod) result.get(0);
        return  fishingRod;
    }

//    public FishingRod getFishingRod(int fishingRodId) {
//        Session session = FactorySession.openSession();
//        HashMap<String, Object> params = new HashMap<String, Object>();
//        params.put("id", fishingRodId);
//        List<Object> result = session.get(FishingRod.class, params);
//        session.close();
//        if (result.isEmpty()) return null;
//        FishingRod fishingRod = (FishingRod) result.get(0);
//        return  fishingRod;
//    }

    public List<FishingRod> getAllFishingRods() {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(FishingRod.class, params);
        session.close();
        List<FishingRod> allFishingRods = (List<FishingRod>)(List<?>) result;
        return allFishingRods;
    }
//
//
//
//
//
//
//    public int getFishingRod(FishingRod r) {
//        if (rods.containsKey(r.getId())) return -1;
//        rods.put(r.getId(), r);
//        return 1;
//    }
//    public Map<String, Fish> getFishMap() { return fish; }
//    public Map<String, FishingRod> getRodsMap() { return rods; }

}

