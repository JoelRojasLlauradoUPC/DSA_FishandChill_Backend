package edu.upc.dsa.util.game;

import edu.upc.dsa.models.Fish;
import edu.upc.dsa.models.User;

public class GameActions {

    // returns: 1 ok, -1 user not found, -2 species unknown
    public int captureFish(User user, Fish fish, double weight) {
        if (user == null) return -1;
        if (fish == null) return -2;
        user.captureFish(fish, weight);
        return 1;
    }
}
