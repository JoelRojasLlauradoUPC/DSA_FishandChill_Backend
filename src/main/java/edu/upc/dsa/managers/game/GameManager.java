package edu.upc.dsa.managers.game;

import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameManager {

    public static List<edu.upc.dsa.models.CapturedFish> getCapturedFishes(User user) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        List<Object> result = session.get(CapturedFish.class, params);
        session.close();
        List<CapturedFish> capturedFishesMapping = (List<CapturedFish>)(List<?>) result;
        List<Fish> allFishes = CatalogManager.getAllFishes();

        // Set fish objects in capturedFishes
        List<edu.upc.dsa.models.CapturedFish> capturedFishes = new ArrayList<>();
        for (CapturedFish cFM : capturedFishesMapping) {
            for (Fish fish : allFishes) {
                if (fish.getId() == cFM.getFishId()) {
                    edu.upc.dsa.models.CapturedFish capturedFish = new edu.upc.dsa.models.CapturedFish(
                            fish, cFM.getWeight(), cFM.getCaptureTime()
                    );
                    capturedFish.setId(cFM.getId());
                    capturedFishes.add(capturedFish);
                    break;
                }
            }
        }
        return capturedFishes;
    }

    public static void captureFish(User user, Fish fish, double weight) {

        Timestamp captureTime = new Timestamp(System.currentTimeMillis());
        CapturedFish capturedFish = new CapturedFish( user.getId(), fish.getId(), weight, captureTime);
        Session session = FactorySession.openSession();
        session.save(capturedFish);
        session.close();
    }

    public static int deleteCapturedFishes(User user) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        List<Object> result = session.get(CapturedFish.class, params);
        for (Object obj : result) {
            CapturedFish cf = (CapturedFish) obj;
            session.delete(cf);
        }
        session.close();
        return result.size();
    }
}
