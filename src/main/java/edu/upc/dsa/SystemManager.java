package edu.upc.dsa;

import edu.upc.dsa.managers.info.InfoManager;
import edu.upc.dsa.managers.catalog.CatalogManager;
import edu.upc.dsa.managers.game.GameManager;
import edu.upc.dsa.managers.shop.ShopManager;
import edu.upc.dsa.managers.user.UserManager;
import edu.upc.dsa.models.CapturedFish;
import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.Team;
import edu.upc.dsa.models.User;
import edu.upc.dsa.services.dto.*;

import org.apache.log4j.Logger;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SystemManager {

    final static Logger logger = Logger.getLogger(SystemManager.class);

    private static final java.util.Map<String, Integer> groupByUser = new java.util.HashMap<>();
    private static final java.util.Map<Integer, java.util.Set<String>> membersByGroup = new java.util.HashMap<>();

    public static int createUser(String username, String password, String email) {
        int res = UserManager.createUser(username, password, email);
        if (res == -1) logger.warn("Username already exists: " + username );
        if (res == -2) logger.warn("Email already exists: " + email);
        else logger.info("User created: " + username + ", email: " + email);
        return res;
    }

    public static edu.upc.dsa.services.dto.User getUser(String username) {
        logger.info("getUser: username=" + username);
        User user = UserManager.getUser(username);
        FishingRod fishingRod = CatalogManager.getFishingRod(user.getEquippedFishingRodId());

        String teamName = "None";
        if (user.getTeamId() !=-1) {
            Team team = UserManager.getTeam(user.getTeamId()) ;
            teamName = team.getName();
        }


        edu.upc.dsa.services.dto.User dtoUser = new edu.upc.dsa.services.dto.User(
                user.getUsername(),
                user.getEmail(),
                user.getCoins(),
                fishingRod != null ? fishingRod.getName() : null,
                user.getAvatarUrl(),
                teamName
        );
        return dtoUser;
    }

    public static void deleteUser(User user) {
        ShopManager.deleteBoughtFishingRods(user);
        GameManager.deleteCapturedFishes(user);
        UserManager.deleteUser(user);
        logger.info("deleteUser: username=" + user.getUsername());
    }

    public static void updateAvatarUrl(User user, String avatarUrl) {
        UserManager.updateAvatarUrl(user, avatarUrl);
        logger.info("updateAvatarUrl: username=" + user.getUsername() + ", avatarUrl=" + avatarUrl);
    }


    public static String login(String username, String password) {
        logger.info("login: username=" + username);
        String token = UserManager.login(username, password);
        if (token == null) logger.warn("Login failed for user: " + username);
        else logger.info("Login ok: " + username);
        return token;
    }

    public static User authenticate(String token) {
        User user = UserManager.authenticate(token);
        if (user == null) logger.warn("Authentication failed for token: " + token);
        else logger.info("Authentication ok: username=" + user.getUsername());
        return user;
    }

    public static void logout(String token) {
        User user = UserManager.authenticate(token);
        if (user != null) {
            UserManager.logout(token);
            logger.info("logout: username=" + user.getUsername());
        } else {
            logger.warn("Logout failed for token: " + token);
        }
    }

    public static edu.upc.dsa.services.dto.Team getTeam(String eventName) {
        Team team = UserManager.getTeam(eventName);
        if (team == null) {
            edu.upc.dsa.services.dto.Team res = new edu.upc.dsa.services.dto.Team();
            res.setTeam("None");
            return res;
        }
        logger.info("getTeamMembers: teamName=" + team.getName());
        edu.upc.dsa.services.dto.Team dtoTeam = new edu.upc.dsa.services.dto.Team();
        dtoTeam.setTeam(team.getName());
        List<User> members = UserManager.getTeamMembers(team.getName());
        List<TeamMember> dtoMembers = new ArrayList<>();
        for (User u : members) {
            int countCapturedFishes =  GameManager.getCapturedFishes(u).size();
            dtoMembers.add(new TeamMember(u.getUsername(), u.getAvatarUrl(), countCapturedFishes));
        }
        dtoTeam.setMembers(dtoMembers);
        return dtoTeam;
    }

    public static int createTeam(String name, String imageUrl) {
        logger.info("createTeam: teamName=" + name);
        int res = UserManager.creatTeam(name, imageUrl);
        if (res == -1) {
            logger.warn("Team already exists: " + name);
        } else {
            logger.info("Team created: " + name);
        }
        return res;
    }


    public static int joinTeam(User user, String teamName) {
        logger.info("joinTeam: username=" + user.getUsername() + ", teamName=" + teamName);
        int res = UserManager.joinTeam(user, teamName);
        if (res == -1) {
            logger.warn("Team not found: " + teamName);
        } else {
            logger.info("User " + user.getUsername() + " joined team: " + teamName);
        }
        return res;
    }

    public static int leaveTeam(User user) {
        logger.info("leaveTeam: username=" + user.getUsername());
        int res = UserManager.leaveTeam(user);
        if (res == -1) {
            logger.warn("User " + user.getUsername() + " is not in a team.");
        } else {
            logger.info("User " + user.getUsername() + " left the team.");
        }
        return res;
    }

    public static List<TeamRanking> getTeamsRanking() {

        List<Team> allTeams = UserManager.getAllTeams();
        List<TeamRanking> teamsRanking = new ArrayList<>();
        logger.info ("getTeamsRanking: total teams=" + allTeams.size());
        for (Team team : allTeams) {
            List<User> members = UserManager.getTeamMembers(team.getName());
            logger.info ("getTeamsRanking: members=" + members.size());
            int totalPoints = 0;
            for (User member : members) {
                totalPoints += GameManager.getCapturedFishes(member).size();

            }
            logger.info ("getTeamsRanking: team=" + team.getName() + ", totalPoints=" + totalPoints);
            TeamRanking tmScore = new TeamRanking(team.getName(), team.getImageUrl(),totalPoints);
            teamsRanking.add(tmScore);
        }

        teamsRanking.sort(Comparator.comparingInt(TeamRanking::getPoints).reversed());
        return teamsRanking;
    }

    public static void subscribeToEvent(User user, int eventId) {
        UserManager.subscribeToEvent(user, eventId);
        logger.info("subscribeToEvent: username=" + user.getUsername() + ", eventId=" + eventId);
    }

    public static List<FishingRod> getOwnedFishingRods(User user) {
        logger.info("Get owned fishing rods for user: username=" + user.getUsername());
        return ShopManager.getBoughtFishingRods(user);
    }

    public static List<CapturedFish> getCapturedFishes(User user) {
        List<CapturedFish> capturedFishes = GameManager.getCapturedFishes(user);
        logger.info("Get captured fishes for user: username=" + user.getUsername());
        return capturedFishes;
    }

    public static Fish getFish(String fishSpeciesName) {
        Fish fish = CatalogManager.getFish(fishSpeciesName);
        if (fish == null) {
            logger.warn("Fish species not found: " + fishSpeciesName);
            return null;
        }
        return fish;
    }

    public static List<Fish> getAllFishes() {
        List<Fish> allFishes = CatalogManager.getAllFishes();
        logger.info("getAllFishes: all fishes count=" + allFishes.size());
        return allFishes;
    }

    public static FishingRod getFishingRod(String fishingRodName) {
        FishingRod rod = CatalogManager.getFishingRod(fishingRodName);
        if (rod == null) {
            logger.warn("Fishing rod not found: " + fishingRodName);
            return null;
        }
        logger.info("getFishingRod: " + fishingRodName);
        return rod;
    }

    public static List<FishingRod> getAllFishingRods() {
        List<FishingRod> allFishingRods = CatalogManager.getAllFishingRods();
        logger.info("getAllFishingRods: all rods count=" + allFishingRods.size());
        return allFishingRods;
    }

    public static int buyFishingRod(User user, FishingRod fishingRod) {
        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        for (FishingRod r : ownedFishingRods) {
            if (r.getName().equals(fishingRod.getName())) {
                logger.warn("Fishing rod already owned: " + fishingRod.getName());
                return -1;
            }
        }
        int res = ShopManager.buyFishingRod(user, fishingRod);
        if (res == -1) {
            logger.warn("User has not enough coins: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
            return -2;
        }

        logger.info("User bought fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName()+", userCoins = "+user.getCoins());
        return 1;
    }

    public static int equipFishingRod(User user, FishingRod fishingRod) {
        List<FishingRod> ownedFishingRods = getOwnedFishingRods(user);
        boolean ownsFishingRod = ownedFishingRods.contains(fishingRod);
        if (!ownsFishingRod) {
            logger.warn("Fishing not owned: " + fishingRod.getName());
            return -1;
        }
        int res = ShopManager.equipFishingRod(user, fishingRod);
        logger.info("User equipped fishing rod: username=" + user.getUsername() + ", rodName=" + fishingRod.getName());
        return res;
    }

    public static void sellCapturedFish(User user, String fishSpeciesName, Timestamp captureTime, int price) {
        int fishId = getFish(fishSpeciesName).getId();
        int result = ShopManager.sellCapturedFish(user, fishId, captureTime, price);

        if (result == -1) {
            logger.warn("Captured fish not found: username=" + user.getUsername() + ", fishSpecies=" + fishSpeciesName + ", captureTime=" + captureTime);
        } else {
            logger.info("User sold captured fish: username=" + user.getUsername() + ", fishSpecies=" + fishSpeciesName + ", captureTime=" + captureTime + ", price=" + price);
        }
    }

    public static void captureFish(User user, Fish fish, double weight) {
        GameManager.captureFish(user, fish, weight);
        logger.info("User captured fish: username=" + user.getUsername() + ", fishSpecies=" + fish.getSpeciesName() + ", weight=" + weight);
    }

    public static List<LeaderboardEntry> getFishLeaderboardCurrent() {

        List<User> users = UserManager.getAllUsers();
        List<LeaderboardEntry> res = new ArrayList<>();

        for (User u : users) {
            int count = GameManager.getCapturedFishes(u).size();
            res.add(new LeaderboardEntry(u.getUsername(), count, u.getAvatarUrl()));
        }

        res.sort(Comparator.comparingInt(LeaderboardEntry::getTotalFishes).reversed());

        return res;
    }

    public static List<Faq> getFaqs() {
        logger.info("Get FAQs");
        return InfoManager.getFaqs();
    }

    public static void postFaqQuestion(edu.upc.dsa.services.dto.Question q) {
        logger.info("Send FAQ question: " + q.getMessage());
        InfoManager.postFaqQuestion(q.getMessage());
    }

    public static List<Video> getVideos() {
        logger.info("Get videos");
        return InfoManager.getVideos();
    }

    public static List<EventUser> getUsersRegisteredInEvent(int eventId) {
        logger.info("Get users registered in event: eventId=" + eventId);
        List<User> users = InfoManager.getUsersInEvent(eventId);
        List<EventUser> res = new ArrayList<>();
        for (User u : users) {
            res.add(new EventUser(u.getUsername(), u.getAvatarUrl()));
        }
        return res;
    }



}
