package edu.upc.dsa.managers.shop;

import edu.upc.dsa.models.FishingRod;
import edu.upc.dsa.models.User;

public class ShopManager {

    public int buyRod(User user, FishingRod rod) {
        // Delegate to user logic and map codes to disambiguated ones
        int res = user.buyFishingRod(rod);
        if (res == 1) return 1;
        if (res == -1) return -3; // not enough coins
        if (res == -2) return -4; // already owned
        return 0; // unknown error
    }
}
