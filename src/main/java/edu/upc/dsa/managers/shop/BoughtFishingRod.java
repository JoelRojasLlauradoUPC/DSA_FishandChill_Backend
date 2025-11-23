package edu.upc.dsa.managers.shop;

public class BoughtFishingRod {
    int id;
    int userId;
    int fishingRodId;

    public BoughtFishingRod() {
    }

    public BoughtFishingRod(int userId, int fishingRodId) {
        this.id = 0;
        this.userId = userId;
        this.fishingRodId = fishingRodId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFishingRodId() {
        return fishingRodId;
    }

    public void setFishingRodId(int fishingRodId) {
        this.fishingRodId = fishingRodId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
