package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class SistemaGestionTest {

    SystemManager sistemaGestion;

    @Before
    public void setUp() {
        this.sistemaGestion = GameManager.getInstance();
        this.sistemaGestion.clear();

        // ---- Creamos datos de prueba ----

        // Posición inicial
        Position p1 = new Position(0.0, 0.0, "lake");
        Position p2 = new Position(10.0, 5.0, "river");

        // Inventarios
        Inventory inv1 = new Inventory("inv1");
        Inventory inv2 = new Inventory("inv2");

        // Usuarios
        User u1 = new User("u1", "PlayerOne", p1, inv1);
        User u2 = new User("u2", "PlayerTwo", p2, inv2);

        this.sistemaGestion.createUser(u1);
        this.sistemaGestion.createUser(u2);

        // Peces
        Fish f1 = new Fish("f1", "Trout", 5.0, 1.2);
        Fish f2 = new Fish("f2", "Salmon", 10.0, 2.5);

        this.sistemaGestion.addFish(f1);
        this.sistemaGestion.addFish(f2);

        // Cañas para u1
        FishingRod r1 = new FishingRod("r1", "Basic Rod", 0);
        FishingRod r2 = new FishingRod("r2", "Pro Rod", 0);

        this.sistemaGestion.addUserRod("u1", r1);
        this.sistemaGestion.addUserRod("u1", r2);
    }

    @After
    public void tearDown() {
        this.sistemaGestion.clear();
    }

    // ---------- TEST USERS ----------

    @Test
    public void testCreateUser() {
        Position p3 = new Position(2.0, 2.0, "sea");
        Inventory inv3 = new Inventory("inv3");
        User u3 = new User("u3", "PlayerThree", p3, inv3);

        this.sistemaGestion.createUser(u3);

        User user = this.sistemaGestion.getUser("u3");
        Assert.assertNotNull(user);
        Assert.assertEquals("PlayerThree", user.getUsername());
        Assert.assertEquals("sea", user.getUserPosition().getZone());
    }

    @Test
    public void testGetUser() {
        User user = this.sistemaGestion.getUser("u1");
        Assert.assertNotNull(user);
        Assert.assertEquals("PlayerOne", user.getUsername());
    }

    @Test
    public void testUpdateUser() {
        User changes = new User();
        changes.setUsername("PlayerOneUpdated");
        changes.setUserPosition(new Position(100.0, 50.0, "deep_lake"));

        User updated = this.sistemaGestion.updateUser("u1", changes);

        Assert.assertNotNull(updated);
        Assert.assertEquals("PlayerOneUpdated", updated.getUsername());
        Assert.assertEquals(100.0, updated.getUserPosition().getX(), 0.001);
        Assert.assertEquals("deep_lake", updated.getUserPosition().getZone());
    }

    @Test
    public void testDeleteUser() {
        this.sistemaGestion.deleteUser("u2");
        User user = this.sistemaGestion.getUser("u2");
        Assert.assertNull(user);
        Assert.assertEquals(1, this.sistemaGestion.sizeUsers());
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = this.sistemaGestion.getAllUsers();
        Assert.assertEquals(2, users.size());
        // como no hay orden garantizado, comprobamos por ids
        boolean hasU1 = users.stream().anyMatch(u -> u.getId().equals("u1"));
        boolean hasU2 = users.stream().anyMatch(u -> u.getId().equals("u2"));
        Assert.assertTrue(hasU1);
        Assert.assertTrue(hasU2);
    }

    // ---------- TEST FISH ----------

    @Test
    public void testGetAllFish() {
        List<Fish> fishList = this.sistemaGestion.getAllFish();
        Assert.assertEquals(2, fishList.size());

        boolean hasTrout = fishList.stream().anyMatch(f -> f.getKind().equals("Trout"));
        boolean hasSalmon = fishList.stream().anyMatch(f -> f.getKind().equals("Salmon"));

        Assert.assertTrue(hasTrout);
        Assert.assertTrue(hasSalmon);
    }

    @Test
    public void testAddAndGetFishByKind() {
        Fish f3 = new Fish("f3", "Carp", 3.0, 0.8);
        this.sistemaGestion.addFish(f3);

        Fish carp = this.sistemaGestion.getFishByKind("Carp");
        Assert.assertNotNull(carp);
        Assert.assertEquals(3.0, carp.getDepth(), 0.001);
        Assert.assertEquals(0.8, carp.getWeight(), 0.001);
    }

    @Test
    public void testUpdateFish() {
        Fish changes = new Fish();
        changes.setDepth(7.5);
        changes.setWeight(1.8);

        Fish updated = this.sistemaGestion.updateFish("Trout", changes);
        Assert.assertNotNull(updated);
        Assert.assertEquals(7.5, updated.getDepth(), 0.001);
        Assert.assertEquals(1.8, updated.getWeight(), 0.001);
    }

    @Test
    public void testDeleteFish() {
        this.sistemaGestion.deleteFish("Salmon");
        Fish salmon = this.sistemaGestion.getFishByKind("Salmon");
        Assert.assertNull(salmon);
    }

    // ---------- TEST RODS / INVENTORY ----------

    @Test
    public void testGetUserRods() {
        List<FishingRod> rods = this.sistemaGestion.getUserRods("u1");
        Assert.assertEquals(2, rods.size());

        boolean hasBasic = rods.stream().anyMatch(r -> r.getKind().equals("Basic Rod"));
        boolean hasPro = rods.stream().anyMatch(r -> r.getKind().equals("Pro Rod"));

        Assert.assertTrue(hasBasic);
        Assert.assertTrue(hasPro);
    }

    @Test
    public void testAddUserRod() {
        FishingRod r3 = new FishingRod("r3", "Ultra Rod", 0);
        this.sistemaGestion.addUserRod("u1", r3);

        List<FishingRod> rods = this.sistemaGestion.getUserRods("u1");
        Assert.assertEquals(3, rods.size());

        boolean hasUltra = rods.stream().anyMatch(r -> r.getId().equals("r3"));
        Assert.assertTrue(hasUltra);
    }

    @Test
    public void testGetUserRod() {
        FishingRod rod = this.sistemaGestion.getUserRod("u1", "r1");
        Assert.assertNotNull(rod);
        Assert.assertEquals("Basic Rod", rod.getKind());
    }

    @Test
    public void testUpdateUserRod() {
        FishingRod changes = new FishingRod();
        changes.setKind("Basic Rod Plus");
        changes.setUsage(5);

        FishingRod updated = this.sistemaGestion.updateUserRod("u1", "r1", changes);

        Assert.assertNotNull(updated);
        Assert.assertEquals("Basic Rod Plus", updated.getKind());
        Assert.assertEquals(5, updated.getUsage());
    }

    @Test
    public void testDeleteUserRod() {
        this.sistemaGestion.deleteUserRod("u1", "r2");

        List<FishingRod> rods = this.sistemaGestion.getUserRods("u1");
        Assert.assertEquals(1, rods.size());

        boolean hasR2 = rods.stream().anyMatch(r -> r.getId().equals("r2"));
        Assert.assertFalse(hasR2);
    }
}
