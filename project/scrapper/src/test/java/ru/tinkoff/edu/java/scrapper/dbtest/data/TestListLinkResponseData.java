package ru.tinkoff.edu.java.scrapper.dbtest.data;

import java.util.Random;

public class TestListLinkResponseData {

    private static final Random RND = new Random();

    public static Long randomId() {
        return RND.nextLong(Long.MAX_VALUE);
    }

}
