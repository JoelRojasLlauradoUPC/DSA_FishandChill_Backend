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

    SystemManager gameManager;

    @Before
    public void setUp() {
        this.gameManager = SystemManager.getInstance();
        // Ensure clean state before each test
        this.gameManager.clear();
    }

    @After
    public void tearDown() {
        this.gameManager.clear();
    }

    // ---------- USERS ----------
    @Test
    public void testUsers_CreateAndDuplicatesAndGetUserNotFound() {
        assertEquals(1, gameManager.createUser("alice", "pass", "alice@example.com"));
        User alice = gameManager.getUser("alice");
        assertNotNull(alice);
        assertEquals("alice", alice.getUsername());
        assertEquals("alice@example.com", alice.getEmail());
        assertEquals(100, alice.getCoins());
        assertNotNull(alice.getInventory());

        // login token checks
        String token = gameManager.login("alice", "pass");
        assertNotNull("Token should be returned for valid credentials", token);
        assertNotNull("Authenticate should return user for valid token", gameManager.authenticate(token));
        assertEquals("alice", gameManager.authenticate(token).getUsername());

        // wrong password should not issue a token
        assertNull("Wrong password must not return a token", gameManager.login("alice", "wrong"));
        // bogus token must not authenticate
        assertNull("Invalid token must not authenticate", gameManager.authenticate("Bearer invalid-token"));

        // duplicates
        assertEquals(-1, gameManager.createUser("alice", "pass2", "alice2@example.com")); // duplicate username
        assertEquals(-1, gameManager.createUser("someone", "pass3", "alice@example.com")); // duplicate email
        assertNull(gameManager.getUser("nope"));
    }

    // ---------- FISH SPECIES ----------
    @Test
    public void testFishSpecies_AddListAndDuplicate() {
        Fish trout = new Fish("Trout", 2, 1.5);
        Fish salmon = new Fish("Salmon", 3, 4.2);
        assertEquals(1, gameManager.addFishSpecies(trout));
        assertEquals(1, gameManager.addFishSpecies(salmon));
        // duplicate species (same id)
        assertEquals(-1, gameManager.addFishSpecies(trout));

        Map<String, Fish> allFish = gameManager.getAllFishSpecies();
        assertEquals(2, allFish.size());
        assertTrue(allFish.containsKey(trout.getId()));
        assertTrue(allFish.containsKey(salmon.getId()));
    }

    // ---------- FISHING RODS ----------
    @Test
    public void testFishingRods_AddListAndDuplicate() {
        FishingRod starter = new FishingRod("Starter", 1.0, 1.0, 1, 100, 10);
        FishingRod pro = new FishingRod("Pro", 1.5, 2.0, 3, 200, 80);
        assertEquals(1, gameManager.addFishingRod(starter));
        assertEquals(1, gameManager.addFishingRod(pro));
        assertEquals(-1, gameManager.addFishingRod(starter)); // duplicate by id

        Map<String, FishingRod> allRods = gameManager.getAllFishingRods();
        assertEquals(2, allRods.size());
        assertTrue(allRods.containsKey(starter.getId()));
        assertTrue(allRods.containsKey(pro.getId()));
    }

    // ---------- INVENTORY ACCESS ----------
    @Test
    public void testInventoryAccess_UserFoundAndNotFound() {
        gameManager.createUser("alice", "pass", "alice@example.com");
        Inventory invAlice = gameManager.getInventory("alice");
        assertNotNull(invAlice);
        assertTrue(invAlice.getUserRods().isEmpty());
        assertTrue(invAlice.getCapturedFishes().isEmpty());
        assertNull(gameManager.getInventory("ghost"));
    }

    // ---------- CAPTURED FISH ----------
    @Test
    public void testCapturedFish_AllCases() {
        // Prepare user and fish species
        gameManager.createUser("alice", "pass", "alice@example.com");
        Fish trout = new Fish("Trout", 2, 1.5);
        Fish salmon = new Fish("Salmon", 3, 4.2);
        gameManager.addFishSpecies(trout);
        gameManager.addFishSpecies(salmon);

        // user not found
        assertEquals(-1, gameManager.capturedFish("no-user", trout.getId(), 2.3));
        // fish not found
        assertEquals(-2, gameManager.capturedFish("alice", "no-fish", 1.0));
        // success
        assertEquals(1, gameManager.capturedFish("alice", salmon.getId(), 5.5));
        Inventory invAfterCapture = gameManager.getInventory("alice");
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
        gameManager.createUser("alice", "pass", "alice@example.com");
        FishingRod starter = new FishingRod("Starter", 1.0, 1.0, 1, 100, 10);
        assertEquals(1, gameManager.addFishingRod(starter));

        // user not found
        assertEquals(-1, gameManager.boughtFishingRod("nobody", starter.getId()));
        // rod not found
        assertEquals(-2, gameManager.boughtFishingRod("alice", "no-rod"));
        // not enough money
        FishingRod luxury = new FishingRod("Luxury", 2.0, 3.0, 5, 300, 150);
        assertEquals(1, gameManager.addFishingRod(luxury));
        assertEquals(-3, gameManager.boughtFishingRod("alice", luxury.getId()));
        assertTrue(gameManager.getInventory("alice").getUserRods().isEmpty());
        assertEquals(100, gameManager.getUser("alice").getCoins());
        // success purchase
        assertEquals(1, gameManager.boughtFishingRod("alice", starter.getId()));
        User aliceAfterBuy = gameManager.getUser("alice");
        assertEquals(90, aliceAfterBuy.getCoins());
        assertTrue(aliceAfterBuy.getInventory().getUserRods().containsKey(starter.getId()));
        // already owned
        assertEquals(-4, gameManager.boughtFishingRod("alice", starter.getId()));
        assertEquals(90, gameManager.getUser("alice").getCoins());
    }

    // ---------- SHOP ----------
    @Test
    public void loadFishingRods() {
        // Prepare
        gameManager.loadRodsDictionary();
        assertEquals(gameManager.getAllFishingRods().size(), 10);
    }

    // ---------- CLEAR ----------
    @Test
    public void testClear_EmptiesAllData() {
        // populate
        gameManager.createUser("alice", "pass", "alice@example.com");
        Fish fish = new Fish("Tuna", 2, 3.2);
        FishingRod rod = new FishingRod("ClearedRod", 1.0, 1.0, 1, 100, 20);
        gameManager.addFishSpecies(fish);
        gameManager.addFishingRod(rod);

        assertNotNull(gameManager.getUser("alice"));
        assertFalse(gameManager.getAllFishSpecies().isEmpty());
        assertFalse(gameManager.getAllFishingRods().isEmpty());

        // clear
        gameManager.clear();

        assertNull(gameManager.getUser("alice"));
        assertTrue(gameManager.getAllFishSpecies().isEmpty());
        assertTrue(gameManager.getAllFishingRods().isEmpty());
    }
}
