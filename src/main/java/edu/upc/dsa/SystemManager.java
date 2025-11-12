package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.util.catalog.CatalogManager;
import edu.upc.dsa.util.game.GameActions;
import edu.upc.dsa.util.shop.ShopManager;
import edu.upc.dsa.util.user.UserManager;
import org.apache.log4j.Logger;

import java.util.Map;

public class SystemManager {

    private static SystemManager instance;
    final static Logger logger = Logger.getLogger(SystemManager.class);

    // Delegated managers
    private final UserManager userManager;
    private final CatalogManager catalogManager;
    private final ShopManager shopManager;
    private final GameActions gameActions;

    private SystemManager() {
        this.catalogManager = new CatalogManager();
        this.userManager = new UserManager();
        this.shopManager = new ShopManager();
        this.gameActions = new GameActions();
    }

    public static SystemManager getInstance() {
        if (instance == null) instance = new SystemManager();
        return instance;
    }

    // ---------- USERS declaration ----------
    public int createUser(String username, String password, String email) {
        int res = userManager.createUser(username, password, email);
        if (res == -1) logger.warn("User already exists: " + username + " / " + email);
        else logger.info("User created: " + username);
        return res;
    }

    public User getUser(String username) {
        logger.info("getUser: username=" + username);
        return userManager.getUser(username);
    }

    public Inventory getInventory(String username) {
        User user = userManager.getUser(username);
        if (user == null) return null;
        return user.getInventory();
    }

    public String login(String username, String password) {
        logger.info("login: username=" + username);
        String token = userManager.login(username, password);
        if (token == null) logger.warn("Login failed for user: " + username);
        else logger.info("Login ok: " + username);
        return token;
    }

    public User authenticate(String token) {
        return userManager.authenticate(token);
    }


    // ---------- CATALOG declaration ----------
    // ---------- FISH ----------
    public int addFishSpecies(Fish fish) {
        int res = catalogManager.addFish(fish);
        if (res == -1) logger.warn("Fish species already exists: " + fish.getId());
        else logger.info("Fish species added: " + fish);
        return res;
    }

    public Map<String, Fish> getAllFishSpecies() {
        return catalogManager.getFishMap();
    }

    // ---------- RODS ----------
    public int addFishingRod(FishingRod rod) {
        int res = catalogManager.addRod(rod);
        if (res == -1) logger.warn("Fishing rod already exists: " + rod.getId());
        else logger.info("Fishing rod added: " + rod);
        return res;
    }

    public Map<String, FishingRod> getAllFishingRods() {
        return catalogManager.getRodsMap();
    }

    // ---------- SHOP ----------

    public int boughtFishingRod(String username, String rodId) {
        User user = userManager.getUser(username);
        if (user == null) {
            logger.warn("User not found: " + username);
            return -1; // user not found
        }
        FishingRod rod = catalogManager.getRodsMap().get(rodId);
        if (rod == null) {
            logger.warn("Fishing rod not found: " + rodId);
            return -2; // rod not found
        }
        int res = shopManager.buyRod(user, rod);
        switch (res) {
            case 1:
                logger.info("User bought fishing rod: username=" + username + ", rodId=" + rodId);
                return 1;
            case -3:
                logger.warn("User has not enough coins: username=" + username + ", rodId=" + rodId);
                return -3;
            case -4:
                logger.warn("User already owns fishing rod: username=" + username + ", rodId=" + rodId);
                return -4;
            default:
                logger.warn("Unknown error buying rod for username=" + username + ", rodId=" + rodId);
                return 0;
        }
    }


    // ---------- USER interactions ----------

    // New: capture with resolved objects (checks can be done by API layer)
    public int capture(User user, Fish fish, double weight) {
        int res = gameActions.captureFish(user, fish, weight);
        if (res == 1) logger.info("Captured fish added to user inventory: username=" + (user!=null?user.getUsername():"null") + ", fishId=" + (fish!=null?fish.getId():"null") + ", weight=" + weight);
        return res;
    }

    public int capturedFish(String username, String fishId, double weight) {
        User user = userManager.getUser(username);
        if (user == null) {
            logger.warn("User not found: " + username);
            return -1;
        }
        Fish fish = catalogManager.getFishMap().get(fishId);
        if (fish == null) {
            logger.warn("Fish species not found: " + fishId);
            return -2;
        }
        int res = gameActions.captureFish(user, fish, weight);
        if (res == 1) logger.info("Captured fish added to user inventory: username=" + username + ", fishId=" + fishId + ", weight=" + weight);
        return res;
    }


    // ---------- Game Manager methods ----------
    public void clear() {
        this.userManager.clear();
        this.catalogManager.getFishMap().clear();
        this.catalogManager.getRodsMap().clear();
        logger.info("GameManager cleared all data.");
    }
}
