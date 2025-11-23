package edu.upc.dsa.managers.user;

import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private final int TOKEN_EXPIRATION_TIME = 1800; // seconds (30 minutes)
    private final Map<String, TokenInfo> tokens;
    // token -> TokenInfo(username, lastAccessEpochSeconds)
    public UserManager() {
        this.tokens = new ConcurrentHashMap<String, TokenInfo>();
    }


    // Simple container for token metadata
    private static class TokenInfo {
        final String username;
        volatile long lastAccessEpochSec;

        TokenInfo(String username) {
            this.username = username;
            this.lastAccessEpochSec = System.currentTimeMillis() / 1000L;
        }

        public void updateLastAccess() {
            this.lastAccessEpochSec = System.currentTimeMillis() / 1000L;
        }
    }

    public int createUser(String username, String password, String email) {

        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        session.get(User.class, params);
        // if user with same username, return -1
        if (!session.get(User.class, params).isEmpty()) return -1;
        params = new HashMap<String, Object>();
        params.put("email", email);
        // if user with same email, return -2
        if (!session.get(User.class, params).isEmpty()) return -2;

        User u = new User(username, password, email);
        session.save(u);
        session.close();
        return 1;
    }

    public User getUser(String username) {
        HashMap <String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        Session session = FactorySession.openSession();
        User user = null;
        List<Object> result = session.get(User.class, params);
        if (!result.isEmpty()) {
            user = (User) result.get(0);
        }
        session.close();
        return user;
    }

    public String login(String username, String password) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        List<Object> result = session.get(User.class, params);
        session.close();
        if (result.isEmpty()) return null; // invalid credentials

        String token = UUID.randomUUID().toString();
        tokens.put(token, new TokenInfo(username));
        return token;
    }

    public User authenticate(String token) { //check if token is valid and not expired, if true reset its timer and return the user
        if (token == null) return null;
        TokenInfo info = tokens.get(token);
        if (info == null) return null;

        long now = System.currentTimeMillis() / 1000L;
        long age = now - info.lastAccessEpochSec;
        if (age > TOKEN_EXPIRATION_TIME) {
            tokens.remove(token);
            return null;
        }
        info.updateLastAccess();
        return getUser(info.username);
    }


    public void clearTokens() {
        tokens.clear();
    }
}
