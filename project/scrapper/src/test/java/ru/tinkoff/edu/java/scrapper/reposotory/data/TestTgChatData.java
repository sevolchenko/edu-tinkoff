package ru.tinkoff.edu.java.scrapper.reposotory.data;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Random;

public class TestTgChatData {

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

    public static List<AddTgChatInput> stabValidResponse(Long tgChatId) {
        return List.of(
                new AddTgChatInput(tgChatId, "username1", randomDate()),
                new AddTgChatInput(tgChatId, "username2", randomDate()),
                new AddTgChatInput(randomId(), "username3", randomDate())
        );
    }

    public static List<AddTgChatInput> stabValidResponse() {
        return List.of(
                new AddTgChatInput(randomId(), "username1", randomDate()),
                new AddTgChatInput(randomId(), "username2", randomDate()),
                new AddTgChatInput(randomId(), "username3", randomDate())
        );
    }

    public static List<AddTgChatInput> stabEmptyResponse() {
        return List.of();
    }

}
