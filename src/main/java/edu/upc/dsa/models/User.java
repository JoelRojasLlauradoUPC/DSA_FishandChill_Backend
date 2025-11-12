package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlTransient;

import static java.util.UUID.randomUUID;

public class User {
    private String id;
    private String username;
    private String password;
    private String email;
    private Inventory inventory;
    private int coins;

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

    @XmlTransient
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

    // Helpers for auth and economy
    public boolean checkPassword(String candidate) {
        return this.password != null && this.password.equals(candidate);
    }

    public void addCoins(int amount) { this.coins += amount; }

    public boolean deductCoins(int amount) {
        if (this.coins < amount) return false;
        this.coins -= amount;
        return true;
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
