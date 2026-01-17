package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.managers.event.EventManager;
import edu.upc.dsa.managers.game.GameManager;
import edu.upc.dsa.managers.shop.ShopManager;
import edu.upc.dsa.managers.user.UserManager;
import edu.upc.dsa.services.dto.EventUser;
import edu.upc.dsa.services.dto.LeaderboardEntry;
import edu.upc.dsa.services.dto.Question;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SystemManager {

    final static Logger logger = Logger.getLogger(SystemManager.class);

    private static final java.util.Map<String, Integer> groupByUser = new java.util.HashMap<>();
    private static final java.util.Map<Integer, java.util.Set<String>> membersByGroup = new java.util.HashMap<>();

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

    public static List<FishingRod> getOwnedFishingRods(User user) {
        logger.info("Get owned fishing rods for user: username=" + user.getUsername());
        return ShopManager.getBoughtFishingRods(user);
    }

    public static List<CapturedFish> getCapturedFishes(User user) {
        List<CapturedFish> capturedFishes = GameManager.getCapturedFishes(user);
        logger.info("Get captured fishes for user: username=" + user.getUsername());
        return capturedFishes;
    }

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

    public static int buyFishingRod(User user, FishingRod fishingRod) {
        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        for (FishingRod r : ownedFishingRods) {
            if (r.getName().equals(fishingRod.getName())) {
                logger.warn("Fishing rod already owned: " + fishingRod.getName());
                return -1;
            }
        }
        int res = ShopManager.buyFishingRod(user, fishingRod);
        if (res == -1) {
            logger.warn("User has not enough coins: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
            return -2;
        }

        logger.info("User bought fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
        return 1;
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
        return res;
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

    public static void captureFish(User user, Fish fish, double weight) {
        GameManager.captureFish(user, fish, weight);
        logger.info("User captured fish: username=" + user.getUsername() + ", fishSpecies=" + fish.getSpeciesName() + ", weight=" + weight);
    }

    public static List<LeaderboardEntry> getFishLeaderboardCurrent(int limit) {
        if (limit < 1) limit = 1;

        List<User> users = UserManager.getAllUsers();
        List<LeaderboardEntry> res = new ArrayList<>();

        for (User u : users) {
            int count = GameManager.getCapturedFishes(u).size();
            res.add(new LeaderboardEntry(u.getUsername(), count));
        }

        res.sort(Comparator.comparingInt(LeaderboardEntry::getTotalFishes).reversed());

        if (res.size() > limit) return res.subList(0, limit);
        return res;
    }

    public static void receiveQuestion(Question q) {
        logger.info("Question received: date=" + q.getDate()
                + ", title=" + q.getTitle()
                + ", sender=" + q.getSender());
    }

    public static List<EventUser> getUsersRegisteredInEvent(String eventId) {
        logger.info("Get users registered in event: eventId=" + eventId);
        return EventManager.getRegisteredUsers(eventId);
    }

    public static List<edu.upc.dsa.services.dto.Group> getAllGroups() {
        List<edu.upc.dsa.services.dto.Group> groups = new ArrayList<>();
        groups.add(new edu.upc.dsa.services.dto.Group(1, "Team Alpha"));
        groups.add(new edu.upc.dsa.services.dto.Group(2, "Deep Sea Hunters"));
        groups.add(new edu.upc.dsa.services.dto.Group(3, "Stormfishers"));
        groups.add(new edu.upc.dsa.services.dto.Group(4, "Meteor Raiders"));
        groups.add(new edu.upc.dsa.services.dto.Group(5, "Old Rod Society"));
        return groups;
    }

    public static boolean groupExists(int groupId) {
        List<edu.upc.dsa.services.dto.Group> groups = getAllGroups();
        for (edu.upc.dsa.services.dto.Group g : groups) {
            if (g.getId() == groupId) return true;
        }
        return false;
    }

    public static int getUserGroupId(User user) {
        if (user == null) return -1;
        Integer gid = groupByUser.get(user.getUsername());
        return gid == null ? -1 : gid;
    }

    public static int joinSingleGroup(User user, int groupId) {
        String username = user.getUsername();

        Integer current = groupByUser.get(username);
        if (current != null) {
            if (current == groupId) return 0;
            return -2;
        }

        groupByUser.put(username, groupId);

        java.util.Set<String> members = membersByGroup.get(groupId);
        if (members == null) {
            members = new java.util.HashSet<>();
            membersByGroup.put(groupId, members);
        }
        members.add(username);

        return 1;
    }

    public static List<edu.upc.dsa.services.dto.GroupUser> getGroupMembers(int groupId) {
        java.util.Set<String> members = membersByGroup.get(groupId);
        List<edu.upc.dsa.services.dto.GroupUser> res = new ArrayList<>();
        if (members == null) return res;

        for (String u : members) {
            res.add(new edu.upc.dsa.services.dto.GroupUser(u));
        }
        return res;
    }
}
