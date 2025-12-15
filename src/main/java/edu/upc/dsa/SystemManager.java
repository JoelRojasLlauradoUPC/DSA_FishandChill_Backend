package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.managers.game.GameManager;
import edu.upc.dsa.managers.shop.ShopManager;
import edu.upc.dsa.managers.user.UserManager;
import edu.upc.dsa.services.dto.Question;
import org.apache.log4j.Logger;

import java.util.List;

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

    public static void deleteUser(User user) {
        ShopManager.deleteBoughtFishingRods(user);
        GameManager.deleteCapturedFishes(user);
        UserManager.deleteUser(user);
        logger.info("deleteUser: username=" + user.getUsername());
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

    public static void logout(String token) {
        User user = UserManager.authenticate(token);
        if (user != null) {
            UserManager.logout(token);
            logger.info("logout: username=" + user.getUsername());
        } else {
            logger.warn("Logout failed for token: " + token);
        }
    }




    // INVENTORY
    public static List<FishingRod> getOwnedFishingRods(User user) {
        logger.info("Get owned fishing rods for user: username=" + user.getUsername());
        return ShopManager.getBoughtFishingRods(user);
    }

    public static List<CapturedFish> getCapturedFishes(User user) {
        List<CapturedFish> capturedFishes = GameManager.getCapturedFishes(user);
        logger.info("Get captured fishes for user: username=" + user.getUsername());
        return capturedFishes;
    }


    // ---------- CATALOG declaration ----------
    // FISH
    public static Fish getFish(String fishSpeciesName) {
        Fish fish = CatalogManager.getFish(fishSpeciesName);
        if (fish == null) {
            logger.warn("Fish species not found: " + fishSpeciesName);
            return null;
        }
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
        if (rod == null) {
            logger.warn("Fishing rod not found: " + fishingRodName);
            return null;
        }
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

    // ---------- QUESTIONS ----------
    public static void receiveQuestion(Question q) {
        logger.info("Question received: date=" + q.getDate()
                + ", title=" + q.getTitle()
                + ", sender=" + q.getSender());
        // solo queda registrado en logs, faltaria BBDD si lo necesitamos
    }

}
