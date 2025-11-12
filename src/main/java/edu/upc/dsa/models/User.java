package edu.upc.dsa.models;

import static java.util.UUID.randomUUID;

public class User {
    String id;
    String username;
    String password;
    String email;
    Inventory inventory;
    int coins;

    public User() {
    }

    public User(String username, String password, String email) {
        this.id = randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.inventory = new Inventory();
        this.coins = 100;
    }

    public String getId() { return id;   }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int buyFishingRod(FishingRod fishingRod) {
        //check if user has enough money
        if (this.coins < fishingRod.getPrice()) {
            return -1; // Not enough money
        } else if (this.inventory.getUserRods().containsKey(fishingRod.getId())) {
            return -2; // Fishing rod already owned
        } else {
            this.coins -= fishingRod.getPrice();
            this.inventory.addFishingRod(fishingRod);
            return 1; // Fishing rod bought successfully
        }
    }

    public void captureFish(Fish fish, double weight) {
        CapturedFish capturedFish = new CapturedFish(fish, weight, new java.sql.Timestamp(System.currentTimeMillis()));
        this.inventory.addCapturedFish(capturedFish);
    }
}
