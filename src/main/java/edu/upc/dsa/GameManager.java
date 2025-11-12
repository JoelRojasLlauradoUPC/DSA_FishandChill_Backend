package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class GameManager {

    private static GameManager instance;
    final static Logger logger = Logger.getLogger(GameManager.class);

    // MAIN DATA STRUCTURES
    Map<String, User> users;                     // username -> User
    Map<String, Fish> fishSpecies;                // fishId -> Fish
    Map<String, FishingRod> fishingRods;    // rodId -> FishingRod



    private GameManager() {
        this.users = new HashMap<>();
        this.fishSpecies = new HashMap<>();
        this.fishingRods = new HashMap<>();
    }

    public static GameManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    // ---------- USERS declaration ----------
    public int createUser(String username, String password, String email) {
        //check if username or email already exists
        for (User u : users.values()) {
            if (u.getUsername().equals(username) || u.getEmail().equals(email)) {
                logger.warn("User already exists: " + username + " / " + email);
                return -1; // User already exists
            }
        }
        User newUser = new User(username, password, email);
        users.put(username, newUser);
        logger.info("User created: " + username);
        return 1; // User created successfully
    }

    public User getUser(String username) {
        logger.info("getUser: username=" + username);
        return users.get(username);
    }

// ---------- FISH ----------

    public int  addFishSpecies(Fish fish) {
        logger.info("addFishSpecies: fishId=" + fish.getId());
        if (fishSpecies.containsKey(fish.getId())) {
            logger.warn("Fish species already exists: " + fish.getId());
            return -1; // Fish species already exists
        }
        fishSpecies.put(fish.getId(), fish);
        logger.info("Fish species added: " + fish);
        return 1; // Fish species added successfully
    }

    public Map<String, Fish> getAllFishSpecies() {
        logger.info("getAllFishSpecies");
        return fishSpecies;
    }

// ---------- RODS ----------

    public int addFishingRod(FishingRod rod) {
        logger.info("addFishingRod: rodId=" + rod.getId());
        if (fishingRods.containsKey(rod.getId())) {
            logger.warn("Fishing rod already exists: " + rod.getId());
            return -1; // Fishing rod already exists
        }
        fishingRods.put(rod.getId(), rod);
        logger.info("Fishing rod added: " + rod);
        return 1; // Fishing rod added successfully
    }

    public Map<String, FishingRod> getAllFishingRods() {
        logger.info("getAllFishingRods");
        return fishingRods;
    }

    // ---------- USER interactions ----------

    public Inventory getInventory(String username) {
        logger.info("getUserInventory: username=" + username);
        User user = users.get(username);
        if (user == null) {
            logger.warn("User not found: " + username);
            return null; // User not found
        }
        return user.getInventory();
    }

    public int capturedFish(String username, String fishId, double weight) {
        logger.info("addCapturedFishToUserInventory: username=" + username + ", fishId=" + fishId);
        User user = users.get(username);
        if (user == null) {
            logger.warn("User not found: " + username);
            return -1; // User not found
        } else if (!fishSpecies.containsKey(fishId)) {
            logger.warn("Fish species not found: " + fishId);
            return -2; // Fish species not found
        }
        Fish fish = fishSpecies.get(fishId);
        user.captureFish(fish, weight);
        logger.info("Captured fish added to user inventory: " + fish.getSpeciesName() + ", weight=" + weight);
        return 1; // Captured fish added successfully
    }

    public int boughtFishingRod(String username, String rodId) {
        logger.info("addRodToUserInventory: username=" + username + ", rodId=" + rodId);
        User user = users.get(username);
        FishingRod rod = fishingRods.get(rodId);
        if (user == null) {
            logger.warn("User not found: " + username);
            return -1; // User not found
        } else if (rod == null) {
            logger.warn("Fishing rod not found: " + rodId);
            return -2; // Fishing rod not found
        }
        int buyResult = user.buyFishingRod(rod);
        if (buyResult == -1) {
            logger.warn("Not enough money for user: " + username + ", rodName=" + rod.getName());
            return buyResult; // Propagate error code from buyFishingRod
        } else if (buyResult == -2) {
            logger.warn("Fishing rod already owned by user: " + username + ", rodName=" + rod.getName());
            return -2; // Fishing rod already owned
        }
        logger.info("User bought fishing rod: " + username + ", rodName=" + rod.getName());
        return 1; // Fishing rod added successfully
    }


    // ---------- SHOP interactions ----------
    public void loadRodsDictionary() {
        logger.info("Loading rods from dictionary file...");

        try (InputStream input = getClass().getResourceAsStream("/rodsDictionary.properties")) {

            if (input == null) {
                logger.error("Could not find rodsDictionary.properties in /resources folder!");
                return;
            }

            logger.info("rodsDictionary.properties found. Reading data...");

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            List<String> values = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Ignore empty or commented lines
                if (line.isEmpty() || line.startsWith("#")) continue;

                values.add(line);

                // Each rod has 7 values (id, name, speed, power, rarity, durability, price)
                if (values.size() == 7) {
                    String id = values.get(0);
                    String name = values.get(1);
                    double speed = Double.parseDouble(values.get(2));
                    double power = Double.parseDouble(values.get(3));
                    int rarity = Integer.parseInt(values.get(4));
                    int durability = Integer.parseInt(values.get(5));
                    int price = Integer.parseInt(values.get(6));

                    FishingRod rod = new FishingRod(name, speed, power, rarity, durability, price);
                    fishingRods.put(rod.getId(), rod);
                    logger.info("Loaded rod: " + rod.getId() + " → " + name);

                    values.clear(); // ready for next rod
                }
            }

            logger.info("Successfully loaded " + fishingRods.size() + " fishing rods from dictionary.");

        } catch (Exception e) {
            logger.error("Error loading rods dictionary: " + e.getMessage(), e);
        }
    }

    /*public List<FishingRod> filterNotBoughtRods(String userID){

    }*/

    // ---------- Game Manager methods ----------
    public void clear() {
        this.users.clear();
        this.fishSpecies.clear();
        this.fishingRods.clear();
        logger.info("GameManager cleared all data.");
    }
}
