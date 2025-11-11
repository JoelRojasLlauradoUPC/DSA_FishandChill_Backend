package edu.upc.dsa.models;

public class User {
    String id;
    String username;
    Position userPosition;
    Inventory userInventory;

    public User() {
    }

    public User(String id, String username, Position userPosition, Inventory userInventory) {
        this.id = id;
        this.username = username;
        this.userPosition = userPosition;
        this.userInventory = userInventory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Position getUserPosition() {
        return userPosition;
    }

    public void setUserPosition(Position userPosition) {
        this.userPosition = userPosition;
    }

    public Inventory getUserInventory() {
        return userInventory;
    }

    public void setUserInventory(Inventory userInventory) {
        this.userInventory = userInventory;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", userPosition=" + userPosition +
                ", userInventory=" + userInventory +
                '}';
    }
}
