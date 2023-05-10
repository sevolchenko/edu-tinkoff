package ru.tinkoff.edu.java.scrapper.reposotory.data;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Random;

public class TestDatesData {

    private static final Random RND = new Random();
    public static final OffsetDateTime startDate = OffsetDateTime.of(LocalDateTime.of(1970, 1, 1, 0, 0, 0), ZoneOffset.UTC);
    public static final OffsetDateTime endDate = OffsetDateTime.of(LocalDateTime.of(2050, 12, 31, 23, 59), ZoneOffset.UTC);

    public static OffsetDateTime randomDate() {
        long startEpochSecond = startDate.toEpochSecond();
        long endEpochSecond = endDate.toEpochSecond();
        long randomSecond = RND.nextLong(startEpochSecond, endEpochSecond);

        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

}
