package edu.upc.dsa.models;

public class Event {
    int id;
    int userId;

    public Event() {
    }

    public Event(int userId, int id) {
        this.userId = userId;
        this.id = id;
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
}
