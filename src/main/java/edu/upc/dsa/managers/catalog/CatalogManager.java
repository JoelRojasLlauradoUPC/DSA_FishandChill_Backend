package edu.upc.dsa.managers.catalog;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.orm.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CatalogManager {
    //FISHES
    public static Fish getFish(String fishSpeciesName) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("speciesName", fishSpeciesName);
        List<Object> result = session.get(Fish.class, params);
        session.close();
        if (result.isEmpty()) return null;
        Fish fish = (Fish) result.get(0);
        return fish;
    }


    public static List<Fish> getAllFishes() {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(Fish.class, params);
        session.close();
        List<Fish> allFishes = (List<Fish>)(List<?>) result;
        return allFishes;
    }

    //FISHING RODS
    public static FishingRod getFishingRod(String fishingRodName) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", fishingRodName);
        List<Object> result = session.get(FishingRod.class, params);
        session.close();
        if (result.isEmpty()) return null;
        FishingRod fishingRod = (FishingRod) result.get(0);
        return  fishingRod;
    }

    public static List<FishingRod> getAllFishingRods() {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        List<Object> result = session.get(FishingRod.class, params);
        session.close();
        List<FishingRod> allFishingRods = (List<FishingRod>)(List<?>) result;
        return allFishingRods;
    }

}

