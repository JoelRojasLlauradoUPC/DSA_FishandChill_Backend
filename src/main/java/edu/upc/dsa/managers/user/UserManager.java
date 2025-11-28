package edu.upc.dsa.managers.user;

import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {
    private static int TOKEN_EXPIRATION_TIME = 1800; // seconds (30 minutes)

    public static int createUser(String username, String password, String email) {
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

    public static User getUser(String username) {
        Session session = FactorySession.openSession();
        HashMap <String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        User user = null;
        List<Object> result = session.get(User.class, params);
        if (!result.isEmpty()) {
            user = (User) result.get(0);
        }
        session.close();
        return user;
    }

    public static void deleteUser(User user) {
        Session session = FactorySession.openSession();
        //delete tokens associated to the user
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("userId", user.getId());
        List<Object> result = session.get(Token.class, params);
        for (Object obj : result) {
            Token token = (Token) obj;
            session.delete(token);
        }
        //delete the user
        session.delete(user);
        session.close();
    }

    public static String login(String username, String password) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        params.put("password", password);
        List<Object> result = session.get(User.class, params);
        if (result.isEmpty()) return null; // invalid credentials

        User user = (User) result.get(0);


        Token token = new Token(user.getId());
        session.save(token);
        session.close();

        return token.getToken();
    }

    public static User authenticate(String token) { //check if token is valid and not expired, if true reset its timer and return the user
        if (token == null) return null;

        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        List<Object> result = session.get(Token.class, params);
        session.close();
        if (result.isEmpty()) {
            return null;
        }
        Token t = (Token) result.get(0);

        session = FactorySession.openSession();
        long now = System.currentTimeMillis() / 1000L;
        long age = now - t.getLastAccess().getTime()/1000L;
        if (age > TOKEN_EXPIRATION_TIME) {
            session.delete(t);
            return null;
        }
        t.updateLastAccess();
        session.update(t);
        params = new HashMap<String, Object>();
        params.put("id", t.getUserId());
        User user = (User) session.get(User.class, params).get(0);
        session.close();
        return user;
    }

    public static void logout(String token) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("token", token);
        List<Object> result = session.get(Token.class, params);
        if (!result.isEmpty()) {
            Token t = (Token) result.get(0);
            session.delete(t);
        }
        session.close();
    }


}
