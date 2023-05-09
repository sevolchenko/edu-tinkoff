package ru.tinkoff.edu.java.scrapper.reposotory.data;

import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubLinkState;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Random;

import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;

public class TestLinkData {

    private static final Random RND = new Random();

    public static Long randomId() {
        return RND.nextLong(Long.MAX_VALUE);
    }

    public static ILinkState randomState() {
        return new GitHubLinkState(randomDate(), RND.nextInt(100));
    }

    public static ILinkState randomUpdatedState(GitHubLinkState gitHubLinkState) {
        return new GitHubLinkState(gitHubLinkState.pushedAt().plus(Duration.ofDays(1)), gitHubLinkState.branchesCount());
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
        return buildLinkResponse(url, randomState(), randomDate(), OffsetDateTime.now());
    }

    public static AddLinkInput buildLinkResponse(String url, ILinkState state, OffsetDateTime lastScannedAt, OffsetDateTime createdAt) {
        return new AddLinkInput(url, state, lastScannedAt, createdAt);
    }
}
