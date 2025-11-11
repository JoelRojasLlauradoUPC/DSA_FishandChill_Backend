package edu.upc.dsa;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.User;

import java.util.List;

public interface SystemManager {

    // ---------- USERS ----------

    /**
     * Crea un nuevo usuario.
     */
    User createUser(User user);

    /**
     * Obtiene un usuario por su id.
     */
    User getUser(String userId);

    /**
     * Actualiza parcialmente un usuario existente.
     */
    User updateUser(String userId, User user);

    /**
     * Elimina un usuario por su id.
     */
    void deleteUser(String userId);

    /**
     * Devuelve todos los usuarios.
     */
    List<User> getAllUsers();


    // ---------- FISH ----------

    /**
     * Devuelve todos los peces disponibles en el sistema.
     */
    List<Fish> getAllFish();

    /**
     * Añade un nuevo pez al sistema.
     */
    Fish addFish(Fish fish);

    /**
     * Obtiene un pez por su tipo (kind).
     */
    Fish getFishByKind(String kind);

    /**
     * Actualiza la información de un pez identificado por su tipo (kind).
     */
    Fish updateFish(String kind, Fish fish);

    /**
     * Elimina un pez por su tipo (kind).
     */
    void deleteFish(String kind);


    // ---------- RODS (INVENTARIO DE USUARIO) ----------

    /**
     * Devuelve todas las cañas de un usuario.
     */
    List<FishingRod> getUserRods(String userId);

    /**
     * Añade una caña al inventario de un usuario.
     */
    FishingRod addUserRod(String userId, FishingRod rod);

    /**
     * Obtiene una caña concreta de un usuario por id de caña.
     */
    FishingRod getUserRod(String userId, String rodId);

    /**
     * Actualiza una caña concreta del inventario de un usuario.
     */
    FishingRod updateUserRod(String userId, String rodId, FishingRod rod);

    /**
     * Elimina una caña del inventario de un usuario.
     */
    void deleteUserRod(String userId, String rodId);


    // ---------- UTIL ----------

    /**
     * Limpia todas las estructuras (para tests, reset, etc.).
     */
    void clear();

    /**
     * Número de usuarios actuales en el sistema.
     */
    int sizeUsers();
}
