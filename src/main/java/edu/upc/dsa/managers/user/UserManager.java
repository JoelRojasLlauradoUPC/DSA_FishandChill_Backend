package edu.upc.dsa.managers.user;

import edu.upc.dsa.managers.shop.BoughtFishingRod;
import edu.upc.dsa.models.Event;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.Team;
import edu.upc.dsa.models.User;
import edu.upc.dsa.orm.*;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

import static java.lang.System.out;


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
        //fishing rod default id = 1
        session.save(u);
        u = (User) session.get(User.class, params).get(0);
        BoughtFishingRod boughtFishingRod = new BoughtFishingRod(u.getId(), 1);
        session.save(boughtFishingRod);
        session.close();
        return 1;
    }

    public static User getUser(String username) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("username", username);
        User user = null;
        List<Object> result = session.get(User.class, params);
        session.close();
        if (!result.isEmpty()) {
            user = (User) result.get(0);
        }
        return user;
    }

    public static User getUser(int userId) {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", userId);
        User user = null;
        List<Object> result = session.get(User.class, params);
        session.close();
        if (!result.isEmpty()) {
            user = (User) result.get(0);
        }
        return user;
    }

    public static void updateAvatarUrl(User user, String avatarUrl) {
        user.setAvatarUrl(avatarUrl);
        Session session = FactorySession.openSession();
        session.update(user);
        session.close();
    }

    public static int creatTeam(String name, String imageUrl)
    {
        if (getTeam(name) != null) return -1;
        Session session = FactorySession.openSession();
        Team team = new Team();
        team.setName(name);
        team.setImageUrl(imageUrl);
        session.save(team);
        session.close();
        return 1;
    }

    public static Team getTeam(String name)
    {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        Team team = null;
        List<Object> result = session.get(Team.class, params);
        session.close();
        if (!result.isEmpty()) {
            team = (Team) result.get(0);
        }
        else return null;
        return team;
    }

    public static Team getTeam(int id)
    {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        Team team = null;
        List<Object> result = session.get(Team.class, params);
        session.close();
        if (!result.isEmpty()) {
            team = (Team) result.get(0);
        }
        return team;
    }

    public static int joinTeam(User user, String teamName)
    {
        Team team = getTeam(teamName);
        if (team == null) return -1; //team does not exist
        user.setTeamId(team.getId());
        Session session = FactorySession.openSession();
        session.update(user);
        session.close();
        return 1; //user joined team successfully
    }

    public static int leaveTeam(User user)
    {
        if (user.getTeamId() == -1) return -1; //user is not in a team
        user.setTeamId(-1);
        Session session = FactorySession.openSession();
        session.update(user);
        session.close();
        return 1; //user left team successfully
    }

    public static List<User> getTeamMembers(String name)
    {
        Session session = FactorySession.openSession();
        Team team = getTeam(name);
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("teamId", team.getId());
        List<Object> result = session.get(User.class, params);
        List<User> teamMembers = new  ArrayList<>();
        session.close();
        if (!result.isEmpty()) {
            for (Object obj : result)
            {
                User user = (User) obj;
                teamMembers.add(user);
            }
        }
        return teamMembers;
    }

    public static List<Team> getAllTeams() {
        Session session = FactorySession.openSession();
        HashMap<String, Object> params = new HashMap<>();
        List<Object> result = session.get(Team.class, params);
        session.close();
        List<Team> teams = new ArrayList<>();
        for (Object o : result) {
            teams.add((Team) o);
        }
        return teams;
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
        long age = now - t.getLastAccess().getTime() / 1000L;
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

    public static List<User> getAllUsers() {
        Session session = FactorySession.openSession();

        // Normalmente, params vacío => traer todos los registros de esa clase
        HashMap<String, Object> params = new HashMap<>();
        List<Object> result = session.get(User.class, params);

        session.close();

        List<User> users = new ArrayList<>();
        for (Object o : result) {
            users.add((User) o);
        }
        return users;
    }

    public static void subscribeToEvent(User user, int eventId) {
        Session session = FactorySession.openSession();
        // Check if already subscribed
        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("eventId", eventId);
        params.put("userId", user.getId());
        List<Object> result = session.get(Event.class, params);
        if (!result.isEmpty()) {
            session.close();
            out.println("User " + user.getId() + " is already subscribed to event " + eventId);
            return; // already subscribed
        }
        out.println("Subscribing user " + user.getId() + " to event " + eventId);
        Event eventUser = new Event(eventId, user.getId());

        session.save(eventUser);
        session.close();
    }
}



