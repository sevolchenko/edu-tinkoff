package ru.tinkoff.edu.java.scrapper.reposotory.data;

import java.util.Random;

public class TestSubscriptionData {

    private static final Random RND = new Random();

    public static int random(int bound) {
        return RND.nextInt(bound);
    }
}
