package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlTransient;

import static java.util.UUID.randomUUID;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private int coins;
    private int equippedFishingRodId;
    private int teamId;
    private String avatarUrl;

    public User() {
    }

    public User(String username, String password, String email) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.email = email;
        this.coins = 0;
        this.equippedFishingRodId = 1; // Default fishing rod
        this.teamId = -1;
        this.avatarUrl = "https://api.dicebear.com/9.x/avataaars/svg?seed=113";
    }

    public int getId() { return id;   }

    public void setId(int id) { this.id = id; }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public int getEquippedFishingRodId() {
        return equippedFishingRodId;
    }

    public void setEquippedFishingRodId(int equippedFishingRodId) {
        this.equippedFishingRodId = equippedFishingRodId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String imageUrl) {
        this.avatarUrl = imageUrl;
    }
}
