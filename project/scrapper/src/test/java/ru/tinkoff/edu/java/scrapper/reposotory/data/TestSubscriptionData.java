package ru.tinkoff.edu.java.scrapper.reposotory.data;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;

public class TestSubscriptionData {

    private static final Random RND = new Random();

    public static int random(int bound) {
        return RND.nextInt(bound);
    }
}
