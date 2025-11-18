package edu.upc.dsa;

import edu.upc.dsa.models.CapturedFish;
import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.Inventory;
import edu.upc.dsa.models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SystemManagerTest {

    SystemManager systemManager;

    @Before
    public void setUp() {
        this.systemManager = SystemManager.getInstance();
        // Ensure clean state before each test
        this.systemManager.clear();
    }

    @After
    public void tearDown() {
        this.systemManager.clear();
    }

    // ---------- USERS ----------
    @Test
    public void testUsers_CreateAndDuplicatesAndGetUserNotFound() {
        assertEquals(1, systemManager.createUser("alice", "pass", "alice@example.com"));
        User alice = systemManager.getUser("alice");
        assertNotNull(alice);
        assertEquals("alice", alice.getUsername());
        assertEquals("alice@example.com", alice.getEmail());
        assertEquals(1000, alice.getCoins());
        assertNotNull(alice.getInventory());

        // login token checks
        String token = systemManager.login("alice", "pass");
        assertNotNull("Token should be returned for valid credentials", token);
        assertNotNull("Authenticate should return user for valid token", systemManager.authenticate(token));
        assertEquals("alice", systemManager.authenticate(token).getUsername());

        // wrong password should not issue a token
        assertNull("Wrong password must not return a token", systemManager.login("alice", "wrong"));
        // bogus token must not authenticate
        assertNull("Invalid token must not authenticate", systemManager.authenticate("Bearer invalid-token"));

        // duplicates
        assertEquals(-1, systemManager.createUser("alice", "pass2", "alice2@example.com")); // duplicate username
        assertEquals(-1, systemManager.createUser("someone", "pass3", "alice@example.com")); // duplicate email
        assertNull(systemManager.getUser("nope"));
    }

    // ---------- FISH SPECIES ----------
    @Test
    public void testFishSpecies_AddListAndDuplicate() {
        Fish trout = new Fish("Trout", 2, 1.5);
        Fish salmon = new Fish("Salmon", 3, 4.2);
        assertEquals(1, systemManager.addFishSpecies(trout));
        assertEquals(1, systemManager.addFishSpecies(salmon));
        // duplicate species (same id)
        assertEquals(-1, systemManager.addFishSpecies(trout));

        Map<String, Fish> allFish = systemManager.getAllFishSpecies();
        assertEquals(2, allFish.size());
        assertTrue(allFish.containsKey(trout.getId()));
        assertTrue(allFish.containsKey(salmon.getId()));
    }

    // ---------- FISHING RODS ----------
    @Test
    public void testFishingRods_AddListAndDuplicate() {
        FishingRod starter = new FishingRod("Starter", 1.0, 1.0, 1, 100, 10);
        FishingRod pro = new FishingRod("Pro", 1.5, 2.0, 3, 200, 80);
        assertEquals(1, systemManager.addFishingRod(starter));
        assertEquals(1, systemManager.addFishingRod(pro));
        assertEquals(-1, systemManager.addFishingRod(starter)); // duplicate by id

        Map<String, FishingRod> allRods = systemManager.getAllFishingRods();
        assertEquals(2, allRods.size());
        assertTrue(allRods.containsKey(starter.getId()));
        assertTrue(allRods.containsKey(pro.getId()));
    }

    // ---------- INVENTORY ACCESS ----------
    @Test
    public void testInventoryAccess_UserFoundAndNotFound() {
        systemManager.createUser("alice", "pass", "alice@example.com");
        Inventory invAlice = systemManager.getInventory("alice");
        assertNotNull(invAlice);
        assertTrue(invAlice.getUserRods().isEmpty());
        assertTrue(invAlice.getCapturedFishes().isEmpty());
        assertNull(systemManager.getInventory("ghost"));
    }

    // ---------- CAPTURED FISH ----------
    @Test
    public void testCapturedFish_AllCases() {
        // Prepare user and fish species
        systemManager.createUser("alice", "pass", "alice@example.com");
        Fish trout = new Fish("Trout", 2, 1.5);
        Fish salmon = new Fish("Salmon", 3, 4.2);
        systemManager.addFishSpecies(trout);
        systemManager.addFishSpecies(salmon);

        // user not found
        assertEquals(-1, systemManager.capturedFish("no-user", trout.getId(), 2.3));
        // fish not found
        assertEquals(-2, systemManager.capturedFish("alice", "no-fish", 1.0));
        // success
        assertEquals(1, systemManager.capturedFish("alice", salmon.getId(), 5.5));
        Inventory invAfterCapture = systemManager.getInventory("alice");
        assertTrue(invAfterCapture.getCapturedFishes().containsKey(salmon.getId()));
        List<CapturedFish> captures = invAfterCapture.getCapturedFishes().get(salmon.getId());
        assertEquals(1, captures.size());
        assertEquals(5.5, captures.get(0).getWeight(), 0.0001);
        assertEquals(salmon.getId(), captures.get(0).getSpeciesFish().getId());
    }

    // ---------- BUY FISHING ROD ----------
    @Test
    public void testBoughtFishingRod_AllCases() {
        // Prepare
        systemManager.createUser("alice", "pass", "alice@example.com");
        FishingRod starter = new FishingRod("Starter", 1.0, 1.0, 1, 100, 100);
        assertEquals(1, systemManager.addFishingRod(starter));

        // user not found
        assertEquals(-1, systemManager.boughtFishingRod("nobody", starter.getId()));
        // rod not found
        assertEquals(-2, systemManager.boughtFishingRod("alice", "no-rod"));
        // not enough money
        FishingRod luxury = new FishingRod("Luxury", 2.0, 3.0, 5, 300, 1500);
        assertEquals(1, systemManager.addFishingRod(luxury));
        assertEquals(-3, systemManager.boughtFishingRod("alice", luxury.getId()));
        assertTrue(systemManager.getInventory("alice").getUserRods().isEmpty());
        assertEquals(1000, systemManager.getUser("alice").getCoins());
        // success purchase
        assertEquals(1, systemManager.boughtFishingRod("alice", starter.getId()));
        User aliceAfterBuy = systemManager.getUser("alice");
        assertEquals(900, aliceAfterBuy.getCoins());
        assertTrue(aliceAfterBuy.getInventory().getUserRods().containsKey(starter.getId()));
        // already owned
        assertEquals(-4, systemManager.boughtFishingRod("alice", starter.getId()));
        assertEquals(900, systemManager.getUser("alice").getCoins());
    }

    // ---------- SHOP ----------
    @Test
    public void loadFishingRods() {
        // Prepare
        systemManager.loadRodsDictionary();
        assertEquals(systemManager.getAllFishingRods().size(), 10);
    }

    // ---------- CLEAR ----------
    @Test
    public void testClear_EmptiesAllData() {
        // populate
        systemManager.createUser("alice", "pass", "alice@example.com");
        Fish fish = new Fish("Tuna", 2, 3.2);
        FishingRod rod = new FishingRod("ClearedRod", 1.0, 1.0, 1, 100, 20);
        systemManager.addFishSpecies(fish);
        systemManager.addFishingRod(rod);

        assertNotNull(systemManager.getUser("alice"));
        assertFalse(systemManager.getAllFishSpecies().isEmpty());
        assertFalse(systemManager.getAllFishingRods().isEmpty());

        // clear
        systemManager.clear();

        assertNull(systemManager.getUser("alice"));
        assertTrue(systemManager.getAllFishSpecies().isEmpty());
        assertTrue(systemManager.getAllFishingRods().isEmpty());
    }
}
