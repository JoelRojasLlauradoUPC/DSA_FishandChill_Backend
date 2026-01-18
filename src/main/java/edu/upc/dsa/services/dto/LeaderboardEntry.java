package edu.upc.dsa.services.dto;

public class LeaderboardEntry {
    private String username;
    private int totalFishes;
    private String avatarUrl;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String username, int totalFishes, String avatarUrl) {
        this.username = username;
        this.totalFishes = totalFishes;
        this.avatarUrl = avatarUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalFishes() {
        return totalFishes;
    }

    public void setTotalFishes(int totalFishes) {
        this.totalFishes = totalFishes;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
