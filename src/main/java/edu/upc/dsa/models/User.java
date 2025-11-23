package edu.upc.dsa.models;

import javax.xml.bind.annotation.XmlTransient;

import static java.util.UUID.randomUUID;

public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private int coins;

    public User() {
    }

    public User(String username, String password, String email) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.email = email;
        this.coins = 1000;
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
}
