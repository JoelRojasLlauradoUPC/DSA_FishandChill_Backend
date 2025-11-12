package edu.upc.dsa.util;

import net.moznion.random.string.RandomStringGenerator;

public class RandomUtils {


    public static String getId() {
        RandomStringGenerator generator = new RandomStringGenerator();
        String ranUUID = generator.generateByRegex("\\w+\\d*[0-9]{0,8}");

        return ranUUID;
    }
}
