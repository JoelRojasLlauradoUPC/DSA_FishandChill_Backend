package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.managers.game.GameManager;
import edu.upc.dsa.managers.shop.ShopManager;
import edu.upc.dsa.managers.user.UserManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SystemManager {

//    private static SystemManager instance;
    final static Logger logger = Logger.getLogger(SystemManager.class);

//
//    public static SystemManager getInstance() {
//        if (instance == null) instance = new SystemManager();
//        return instance;
//    }

    // ---------- USERS ----------

    //AUTHENTICATION
    public static int createUser(String username, String password, String email) {
        int res = UserManager.createUser(username, password, email);
        if (res == -1) logger.warn("Username already exists: " + username );
        if (res == -2) logger.warn("Email already exists: " + email);
        else logger.info("User created: " + username + ", email: " + email);
        return res;
    }

    public static User getUser(String username) {
        logger.info("getUser: username=" + username);
        return UserManager.getUser(username);
    }

    public static String login(String username, String password) {
        logger.info("login: username=" + username);
        String token = UserManager.login(username, password);
        if (token == null) logger.warn("Login failed for user: " + username);
        else logger.info("Login ok: " + username);
        return token;
    }

    public static User authenticate(String token) {
        User user = UserManager.authenticate(token);
        if (user == null) logger.warn("Authentication failed for token: " + token);
        else logger.info("Authentication ok: username=" + user.getUsername());
        return user;
    }


    // INVENTORY
    public static List<FishingRod> getOwnedFishingRods(User user) {
        List<FishingRod> allFishingRods = CatalogManager.getAllFishingRods();
        logger.info("Get owned fishing rods for user: username=" + user.getUsername() + ", totalRodsCount=" + allFishingRods.size());
        return ShopManager.getBoughtFishingRods(user, allFishingRods);
    }

    public static List<CapturedFish> getCapturedFishes(User user) {
        List<Fish> allFishes = getAllFishes();
        List<CapturedFish> capturedFishes = GameManager.getCapturedFishes(user, allFishes);
        logger.info("Get captured fishes for user: username=" + user.getUsername() + ", capturedFishesCount=" + capturedFishes.size());
        return capturedFishes;
    }




    // ---------- CATALOG declaration ----------
    // FISH
    public static Fish getFish(String fishSpeciesName) {
        Fish fish = CatalogManager.getFish(fishSpeciesName);
        if (fish == null) logger.warn("Fish species not found: " + fishSpeciesName);
        return fish;
    }

    public static List<Fish> getAllFishes() {
        List<Fish> allFishes = CatalogManager.getAllFishes();
        logger.info("getAllFishes: all fishes count=" + allFishes.size());
        return allFishes;
    }

    // FISHING RODS
    public static FishingRod getFishingRod(String fishingRodName) {
        FishingRod rod = CatalogManager.getFishingRod(fishingRodName);
        if (rod == null) logger.warn("Fishing rod not found: " + fishingRodName);
        logger.info("getFishingRod: " + fishingRodName);
        return rod;
    }

    public static List<FishingRod> getAllFishingRods() {
        List<FishingRod> allFishingRods = CatalogManager.getAllFishingRods();
        logger.info("getAllFishingRods: all rods count=" + allFishingRods.size());
        return allFishingRods;
    }


    // ---------- SHOP ----------
    public static int buyFishingRod(User user, FishingRod fishingRod) {

        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        for (FishingRod r : ownedFishingRods) {
            if (r.getName().equals(fishingRod.getName())) {
                logger.warn("Fishing rod already owned: " + fishingRod.getName());
                return -1; // already owns the rod
            }
        }
        int res = ShopManager.buyFishingRod(user, fishingRod);
        if (res == -1) {
            logger.warn("User has not enough coins: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
            return -2; // not enough coins
        }

        logger.info("User bought fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
        return 1; // rod bought successfully
    }


    // ---------- GAME ----------


    public static void captureFish(User user, Fish fish, double weight) {
        GameManager.captureFish(user, fish, weight);
        logger.info("User captured fish: username=" + user.getUsername() + ", fishSpecies=" + fish.getSpeciesName() + ", weight=" + weight);
    }


}
