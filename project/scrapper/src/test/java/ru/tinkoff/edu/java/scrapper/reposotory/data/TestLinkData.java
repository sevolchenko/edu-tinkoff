package ru.tinkoff.edu.java.scrapper.reposotory.data;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class TestLinkData {

    private static final Random RND = new Random();

    public static Long randomId() {
        return RND.nextLong(Long.MAX_VALUE);
    }

    public static OffsetDateTime randomDate() {
        long startEpochSecond = LocalDateTime.of(1970, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long endEpochSecond = LocalDateTime.of(2050, 12, 31, 23, 59).toEpochSecond(ZoneOffset.UTC);
        long randomSecond = RND.nextLong(startEpochSecond, endEpochSecond);

        return OffsetDateTime.of(LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.UTC), ZoneOffset.UTC);
    }

    public static List<LinkOutput> stabValidResponse() {
        return List.of(
                buildLinkResponse(null, "https://localhost:8081"),
                buildLinkResponse(null, "https://localhost:8082"),
                buildLinkResponse(null, "https://localhost:8083")
        );
    }

    public static List<LinkOutput> stabEmptyResponse() {
        return List.of();
    }

    public static LinkOutput buildLinkResponse(Long linkId, String url) {
        var response = new LinkOutput();
        response.setLinkId(linkId);
        response.setUrl(url);
        response.setCreatedAt(OffsetDateTime.now());
        response.setLastScannedAt(OffsetDateTime.now());
        return response;
    }
}
