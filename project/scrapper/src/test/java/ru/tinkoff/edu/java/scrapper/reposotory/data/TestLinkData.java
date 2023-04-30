package ru.tinkoff.edu.java.scrapper.reposotory.data;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;

public class TestLinkData {

    private static final Random RND = new Random();

    public static Long randomId() {
        return RND.nextLong(Long.MAX_VALUE);
    }

    public static List<AddLinkInput> stabValidResponse() {
        return List.of(
                buildLinkResponse("https://localhost:8081"),
                buildLinkResponse("https://localhost:8082"),
                buildLinkResponse("https://localhost:8083")
        );
    }

    public static List<AddLinkInput> stabEmptyResponse() {
        return List.of();
    }

    public static AddLinkInput buildLinkResponse(String url) {
        return buildLinkResponse(url, randomDate(), OffsetDateTime.now());
    }

    public static AddLinkInput buildLinkResponse(String url, OffsetDateTime lastScannedAt, OffsetDateTime createdAt) {
        return new AddLinkInput(url, lastScannedAt, createdAt);
    }
}
