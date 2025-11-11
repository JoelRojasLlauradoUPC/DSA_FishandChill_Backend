package edu.upc.dsa;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import org.apache.log4j.Logger;

import java.util.*;

public class GameManager {

    private static GameManager instance;

    // MAPAS PRINCIPALES
    Map<String, User> users;                     // userId -> User
    Map<String, Fish> fishByKind;                // kind -> Fish
    Map<String, List<FishingRod>> rodsByUser;    // userId -> lista de cañas

    final static Logger logger = Logger.getLogger(GameManager.class);

    private GameManager() {
        this.users = new HashMap<>();
        this.fishByKind = new HashMap<>();
        this.rodsByUser = new HashMap<>();
    }

    public static SystemManager getInstance() {
        if (instance == null) instance = new GameManager();
        return instance;
    }

    // ---------- USERS ----------

    @Override
    public User createUser(User user) {
        logger.info("createUser: id=" + user.getId());

        if (user.getUserInventory() == null) {
            // inventario básico por defecto
            user.setUserInventory(new Inventory("inv-" + user.getId()));
        }

        users.put(user.getId(), user);
        // inicializamos también su lista de cañas
        rodsByUser.put(user.getId(), new ArrayList<>());

        logger.info("User añadido: " + user);
        return user;
    }

    @Override
    public User getUser(String userId) {
        logger.info("getUser: id=" + userId);
        return users.get(userId);
    }

    @Override
    public User updateUser(String userId, User user) {
        logger.info("updateUser: id=" + userId);

        User existing = users.get(userId);
        if (existing == null) {
            logger.warn("User no encontrado: " + userId);
            return null;
        }

        if (user.getUsername() != null) {
            existing.setUsername(user.getUsername());
        }
        if (user.getUserPosition() != null) {
            existing.setUserPosition(user.getUserPosition());
        }
        if (user.getUserInventory() != null) {
            existing.setUserInventory(user.getUserInventory());
        }

        logger.info("User actualizado: " + existing);
        return existing;
    }

    @Override
    public void deleteUser(String userId) {
        logger.info("deleteUser: id=" + userId);
        users.remove(userId);
        rodsByUser.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        logger.info("getAllUsers");
        return new ArrayList<>(users.values());
    }

    // ---------- FISH ----------

    @Override
    public List<Fish> getAllFish() {
        logger.info("getAllFish");
        return new ArrayList<>(fishByKind.values());
    }

    @Override
    public Fish addFish(Fish fish) {
        logger.info("addFish: kind=" + fish.getKind());
        fishByKind.put(fish.getKind(), fish);
        return fish;
    }

    @Override
    public Fish getFishByKind(String kind) {
        logger.info("getFishByKind: kind=" + kind);
        return fishByKind.get(kind);
    }

    @Override
    public Fish updateFish(String kind, Fish fish) {
        logger.info("updateFish: kind=" + kind);

        Fish existing = fishByKind.get(kind);
        if (existing == null) {
            logger.warn("Fish no encontrado: " + kind);
            return null;
        }

        if (fish.getDepth() != 0.0) {
            existing.setDepth(fish.getDepth());
        }
        if (fish.getWeight() != 0.0) {
            existing.setWeight(fish.getWeight());
        }

        logger.info("Fish actualizado: " + existing);
        return existing;
    }

    @Override
    public void deleteFish(String kind) {
        logger.info("deleteFish: kind=" + kind);
        fishByKind.remove(kind);
    }

    // ---------- RODS (INVENTARIO USUARIO) ----------

    @Override
    public List<FishingRod> getUserRods(String userId) {
        logger.info("getUserRods: userId=" + userId);
        return rodsByUser.getOrDefault(userId, new ArrayList<>());
    }

    @Override
    public FishingRod addUserRod(String userId, FishingRod rod) {
        logger.info("addUserRod: userId=" + userId + ", rodId=" + rod.getId());

        List<FishingRod> rods = rodsByUser.computeIfAbsent(userId, k -> new ArrayList<>());
        rods.add(rod);
        return rod;
    }

    @Override
    public FishingRod getUserRod(String userId, String rodId) {
        logger.info("getUserRod: userId=" + userId + ", rodId=" + rodId);

        List<FishingRod> rods = rodsByUser.get(userId);
        if (rods == null) return null;

        return rods.stream()
                .filter(r -> r.getId().equals(rodId))
                .findFirst()
                .orElse(null);
    }

    @Override
    public FishingRod updateUserRod(String userId, String rodId, FishingRod rod) {
        logger.info("updateUserRod: userId=" + userId + ", rodId=" + rodId);

        FishingRod existing = getUserRod(userId, rodId);
        if (existing == null) {
            logger.warn("Rod no encontrada para user " + userId + " con id " + rodId);
            return null;
        }

        if (rod.getKind() != null) {
            existing.setKind(rod.getKind());
        }
        if (rod.getUsage() != 0) {
            existing.setUsage(rod.getUsage());
        }

        logger.info("Rod actualizada: " + existing);
        return existing;
    }

    @Override
    public void deleteUserRod(String userId, String rodId) {
        logger.info("deleteUserRod: userId=" + userId + ", rodId=" + rodId);

        List<FishingRod> rods = rodsByUser.get(userId);
        if (rods == null) return;

        rods.removeIf(r -> r.getId().equals(rodId));
    }

    // ---------- UTIL ----------

    @Override
    public void clear() {
        logger.info("clear SistemaGestion");
        users.clear();
        fishByKind.clear();
        rodsByUser.clear();
    }

    @Override
    public int sizeUsers() {
        return users.size();
    }
}
