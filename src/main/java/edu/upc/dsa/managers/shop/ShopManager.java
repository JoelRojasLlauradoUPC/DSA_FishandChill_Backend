package edu.upc.dsa.managers.shop;

import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopManager {

    public ShopManager() {
    }

    public static List<FishingRod> getBoughtFishingRods(User user) {
        List<FishingRod> allFishingRods = CatalogManager.getAllFishingRods();
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        List<Object> result = session.get(BoughtFishingRod.class, params);
        session.close();
        List<BoughtFishingRod> boughtFishingRodsMapping = (List<BoughtFishingRod>)(List<?>) result;

        List<FishingRod> boughtFishingRods = new ArrayList<FishingRod>();
        for (BoughtFishingRod bfrm  : boughtFishingRodsMapping) {
            for (FishingRod fr : allFishingRods) {
                if (bfrm.getFishingRodId() == fr.getId()) {
                    boughtFishingRods.add(fr);
                    break;
                }
            }
        }
        return boughtFishingRods;
    }

    public static int buyFishingRod(User user, FishingRod fishingRod) {
        if (fishingRod.getPrice() > user.getCoins()) {
            return -1; // not enough coins
        }
        // save bought fishing rod
        BoughtFishingRod boughtFishingRod = new BoughtFishingRod(user.getId(), fishingRod.getId());
        Session session = FactorySession.openSession();
        session.save(boughtFishingRod);
        // deduct coins
        user.setCoins(user.getCoins() - fishingRod.getPrice());
        session.update(user);
        session.close();
        return 1; // fishing rod bought successfully
    }

    public static int deleteBoughtFishingRods(User user) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        List<Object> result = session.get(BoughtFishingRod.class, params);
        for (Object obj : result) {
            BoughtFishingRod bfr = (BoughtFishingRod) obj;
            session.delete(bfr);
        }
        session.close();
        return result.size(); // total bought fishing rods deleted
    }

}
