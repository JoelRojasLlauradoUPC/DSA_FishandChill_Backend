package edu.upc.dsa.services.dto;

public class LeaderboardEntry {
    private String username;
    private int totalFishes;

    public LeaderboardEntry() {}

    public LeaderboardEntry(String username, int totalFishes) {
        this.username = username;
        this.totalFishes = totalFishes;
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
}
