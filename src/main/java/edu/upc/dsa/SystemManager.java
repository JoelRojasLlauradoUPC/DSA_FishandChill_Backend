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

    private static SystemManager instance;
    final static Logger logger = Logger.getLogger(SystemManager.class);

    // Delegated managers
    private final UserManager userManager;
    private final CatalogManager catalogManager;
    private final ShopManager shopManager;
    private final GameManager gameManager;

    private SystemManager() {
        this.catalogManager = new CatalogManager();
        this.userManager = new UserManager();
        this.shopManager = new ShopManager();
        this.gameManager = new GameManager();
    }

    public static SystemManager getInstance() {
        if (instance == null) instance = new SystemManager();
        return instance;
    }

    // ---------- USERS ----------

    //AUTHENTICATION
    public int createUser(String username, String password, String email) {
        int res = userManager.createUser(username, password, email);
        if (res == -1) logger.warn("Username already exists: " + username );
        if (res == -2) logger.warn("Email already exists: " + email);
        else logger.info("User created: " + username + ", email: " + email);
        return res;
    }

    public User getUser(String username) {
        logger.info("getUser: username=" + username);
        return userManager.getUser(username);
    }

    public String login(String username, String password) {
        logger.info("login: username=" + username);
        String token = userManager.login(username, password);
        if (token == null) logger.warn("Login failed for user: " + username);
        else logger.info("Login ok: " + username);
        return token;
    }

    public User authenticate(String token) {
        User user = userManager.authenticate(token);
        if (user == null) logger.warn("Authentication failed for token: " + token);
        else logger.info("Authentication ok: username=" + user.getUsername());
        return user;
    }


    // INVENTORY
    public List<FishingRod> getOwnedFishingRods(User user) {
        List<FishingRod> allFishingRods = catalogManager.getAllFishingRods();
        logger.info("Get owned fishing rods for user: username=" + user.getUsername() + ", totalRodsCount=" + allFishingRods.size());
        return shopManager.getBoughtFishingRods(user, allFishingRods);
    }

    public List<CapturedFish> getCapturedFishes(User user) {
        List<Fish> allFishes = getAllFishes();
        List<CapturedFish> capturedFishes = gameManager.getCapturedFishes(user, allFishes);
        logger.info("Get captured fishes for user: username=" + user.getUsername() + ", capturedFishesCount=" + capturedFishes.size());
        return capturedFishes;
    }




    // ---------- CATALOG declaration ----------
    // FISH
    public Fish getFish(String fishSpeciesName) {
        Fish fish = catalogManager.getFish(fishSpeciesName);
        if (fish == null) logger.warn("Fish species not found: " + fishSpeciesName);
        return fish;
    }

    public List<Fish> getAllFishes() {
        List<Fish> allFishes = catalogManager.getAllFishes();
        logger.info("getAllFishes: all fishes count=" + allFishes.size());
        return allFishes;
    }

    // FISHING RODS
    public FishingRod getFishingRod(String fishingRodName) {
        FishingRod rod = catalogManager.getFishingRod(fishingRodName);
        if (rod == null) logger.warn("Fishing rod not found: " + fishingRodName);
        logger.info("getFishingRod: " + fishingRodName);
        return rod;
    }

    public List<FishingRod> getAllFishingRods() {
        List<FishingRod> allFishingRods = catalogManager.getAllFishingRods();
        logger.info("getAllFishingRods: all rods count=" + allFishingRods.size());
        return allFishingRods;
    }


    // ---------- SHOP ----------
    public int buyFishingRod(User user, FishingRod fishingRod) {

        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        for (FishingRod r : ownedFishingRods) {
            if (r.getName().equals(fishingRod.getName())) {
                logger.warn("Fishing rod already owned: " + fishingRod.getName());
                return -1; // already owns the rod
            }
        }
        int res = shopManager.buyFishingRod(user, fishingRod);
        if (res == -1) {
            logger.warn("User has not enough coins: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
            return -2; // not enough coins
        }

        logger.info("User bought fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
        return 1; // rod bought successfully
    }


    // ---------- GAME ----------


    public void captureFish(User user, Fish fish, double weight) {
        gameManager.captureFish(user, fish, weight);
        logger.info("User captured fish: username=" + user.getUsername() + ", fishSpecies=" + fish.getSpeciesName() + ", weight=" + weight);
    }


//    // ---------- Game Manager methods ----------
//    public void clear() {
//        this.userManager.clear();
//        this.catalogManager.getFishMap().clear();
//        this.catalogManager.getRodsMap().clear();
//        logger.info("GameManager cleared all data.");
//    }
}
