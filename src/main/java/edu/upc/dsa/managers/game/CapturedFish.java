package edu.upc.dsa.managers.game;

import java.sql.Timestamp;

public class CapturedFish {
    int id;
    int userId;
    int fishId;
    double weight;
    Timestamp captureTime;

    public CapturedFish() {
    }

    public CapturedFish(int userId, int fishId, double weight, Timestamp captureTime) {
        this.id = 0;
        this.userId = userId;
        this.fishId = fishId;
        this.weight = weight;
        this.captureTime = captureTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFishId() {
        return fishId;
    }

    public void setFishId(int fishId) {
        this.fishId = fishId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Timestamp getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(Timestamp captureTime) {
        this.captureTime = captureTime;
    }
}
