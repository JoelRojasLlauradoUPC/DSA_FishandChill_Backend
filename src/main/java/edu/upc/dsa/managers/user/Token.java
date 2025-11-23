package edu.upc.dsa.managers.user;

import java.sql.Timestamp;
import java.util.UUID;

public class Token {
    int id;
    String token;
    int userId;
    Timestamp lastAccess;

    public Token() {
    }

    public Token(int userId) {
        this.id = 0;
        this.token = UUID.randomUUID().toString();
        this.userId = userId;
        this.lastAccess = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Timestamp lastAccess) {
        this.lastAccess = lastAccess;
    }

    public void updateLastAccess() {
        this.lastAccess = new Timestamp(System.currentTimeMillis());
    }
}
