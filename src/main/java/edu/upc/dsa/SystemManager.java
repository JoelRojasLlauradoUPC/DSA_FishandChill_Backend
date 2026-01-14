package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.managers.game.GameManager;
import edu.upc.dsa.managers.shop.ShopManager;
import edu.upc.dsa.managers.user.UserManager;
import edu.upc.dsa.services.dto.Question;
import org.apache.log4j.Logger;
import edu.upc.dsa.managers.event.EventManager;
import edu.upc.dsa.services.dto.EventUser;

// ✅ NUEVO import (DTO leaderboard)
import edu.upc.dsa.services.dto.LeaderboardEntry;

// ✅ NUEVOS imports
import java.util.ArrayList;
import java.util.Comparator;

import java.sql.Timestamp;
import java.util.List;

public class SystemManager {

    final static Logger logger = Logger.getLogger(SystemManager.class);

    // ---------- USERS ----------

    //AUTHENTICATION
    public static int createUser(String username, String password, String email) {
        int res = UserManager.createUser(username, password, email);
        if (res == -1) logger.warn("Username already exists: " + username );
        if (res == -2) logger.warn("Email already exists: " + email);
        else logger.info("User created: " + username + ", email: " + email);
        return res;
    }

    public static edu.upc.dsa.services.dto.User getUser(String username) {
        logger.info("getUser: username=" + username);
        User user = UserManager.getUser(username);
        FishingRod fishingRod = CatalogManager.getFishingRod(user.getEquippedFishingRodId());
        edu.upc.dsa.services.dto.User dtoUser = new edu.upc.dsa.services.dto.User(
                user.getUsername(),
                user.getEmail(),
                user.getCoins(),
                fishingRod != null ? fishingRod.getName() : null
        );
        return dtoUser;
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

    public static int equipFishingRod(User user, FishingRod fishingRod) {
        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        boolean ownsFishingRod = ownedFishingRods.contains(fishingRod);
        if (!ownsFishingRod) {
            logger.warn("Fishing not owned: " + fishingRod.getName());
            return -1;
        }
        int res = ShopManager.equipFishingRod(user, fishingRod);
        logger.info("User equipped fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName());
        return res; // rod equipped successfully
    }

    public static void sellCapturedFish(User user, String fishSpeciesName, Timestamp captureTime, int price) {
        int fishId = getFish(fishSpeciesName).getId();
        int result = ShopManager.sellCapturedFish(user, fishId, captureTime, price);

        if (result == -1) {
            logger.warn("Captured fish not found: username=" + user.getUsername() + ", fishSpecies=" + fishSpeciesName + ", captureTime=" + captureTime);
        } else {
            logger.info("User sold captured fish: username=" + user.getUsername() + ", fishSpecies=" + fishSpeciesName + ", captureTime=" + captureTime + ", price=" + price);
        }
    }

    // ---------- GAME ----------
    public static void captureFish(User user, Fish fish, double weight) {
        GameManager.captureFish(user, fish, weight);
        logger.info("User captured fish: username=" + user.getUsername() + ", fishSpecies=" + fish.getSpeciesName() + ", weight=" + weight);
    }

    // ✅ LEADERBOARD (SENCILLO): top por peces actuales en inventario
    public static List<LeaderboardEntry> getFishLeaderboardCurrent(int limit) {
        if (limit < 1) limit = 1;

        List<User> users = UserManager.getAllUsers(); // tienes que añadir este método en UserManager
        List<LeaderboardEntry> res = new ArrayList<>();

        for (User u : users) {
            int count = GameManager.getCapturedFishes(u).size();
            res.add(new LeaderboardEntry(u.getUsername(), count));
        }

        res.sort(Comparator.comparingInt(LeaderboardEntry::getTotalFishes).reversed());

        if (res.size() > limit) return res.subList(0, limit);
        return res;
    }

    // ---------- QUESTIONS ----------
    public static void receiveQuestion(Question q) {
        logger.info("Question received: date=" + q.getDate()
                + ", title=" + q.getTitle()
                + ", sender=" + q.getSender());
        // solo queda registrado en logs, faltaria BBDD si lo necesitamos
    }

    // ---------- EVENTS ----------
    public static List<EventUser> getUsersRegisteredInEvent(String eventId) {
        logger.info("Get users registered in event: eventId=" + eventId);
        return EventManager.getRegisteredUsers(eventId);
    }
}
