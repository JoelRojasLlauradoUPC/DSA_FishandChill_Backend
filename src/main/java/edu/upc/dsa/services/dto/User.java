package edu.upc.dsa.services.dto;

import javax.xml.bind.annotation.XmlTransient;

import static java.util.UUID.randomUUID;

public class User {
    private String username;
    private String email;
    private int coins;
    private String equippedFishingRod;
    private String avatarUrl;
    private String teamName;


    public User() {
    }

    public User(String username, String email, int coins, String equippedFishingRod, String avatarUrl, String teamName) {
        this.username = username;
        this.email = email;
        this.coins = coins;
        this.equippedFishingRod = equippedFishingRod;
        this.avatarUrl = avatarUrl;
        this.teamName = teamName;
    }

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

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public String getEquippedFishingRod() { return equippedFishingRod; }

    public void setEquippedFishingRod(String equippedFishingRod) { this.equippedFishingRod = equippedFishingRod; }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
