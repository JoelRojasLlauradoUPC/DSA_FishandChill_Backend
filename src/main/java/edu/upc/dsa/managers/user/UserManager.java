package edu.upc.dsa.managers.user;

import edu.upc.dsa.models.User;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
    private final Map<String, String> tokens = new ConcurrentHashMap<>(); // token -> username

    public int createUser(String username, String password, String email) {
        for (User u : usersByUsername.values()) {
            if (u.getUsername().equals(username) || u.getEmail().equals(email)) return -1;
        }
        User u = new User(username, password, email);
        usersByUsername.put(username, u);
        return 1;
    }
    public User getUser(String username) { return usersByUsername.get(username); }

    public String login(String username, String password) {
        User u = usersByUsername.get(username);
        if (u == null || !u.checkPassword(password)) return null;
        String token = UUID.randomUUID().toString();
        tokens.put(token, username);
        return token;
    }

    public User authenticate(String token) {
        if (token == null) return null;
        String username = tokens.get(token);
        return username == null ? null : usersByUsername.get(username);
    }

    public void clear() {
        usersByUsername.clear();
        tokens.clear();
    }
}

