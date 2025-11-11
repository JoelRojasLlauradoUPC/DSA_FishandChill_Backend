package edu.upc.dsa.models;

import static java.util.UUID.randomUUID;

public class User {
    String id;
    String username;
    String password;
    String email;
    Inventory userInventory;
    int money;

    public User() {
    }

    public User(String username, String password, String email, Inventory userInventory) {
        this.id = randomUUID().toString();
        this.username = username;
        this.password = password;
        this.email = email;
        this.userInventory = userInventory;
        this.money = 100;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Inventory getUserInventory() {
        return userInventory;
    }

    public void setUserInventory(Inventory userInventory) {
        this.userInventory = userInventory;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", userInventory=" + userInventory +
                '}';
    }
}
