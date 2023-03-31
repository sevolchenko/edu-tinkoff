package ru.tinkoff.edu.java.scrapper.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.dto.request.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.GitHubRepositoryResponse;

import java.util.Objects;

@RequiredArgsConstructor
public class GitHubClient implements IGitHubClient {

    private static final String API_URL = "https://api.github.com";

    private final WebClient webClient;

    public static GitHubClient create() {
        return create(API_URL);
    }

    public static GitHubClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();

        return new GitHubClient(webClient);
    }

    public GitHubRepositoryResponse fetchRepository(GitHubRepositoryRequest gitHubRepositoryRequest) {
        String path = "/repos/{owner}/{repo}";
        GitHubRepositoryResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(gitHubRepositoryRequest.owner(), gitHubRepositoryRequest.repository()))
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class)
                .block();
        Objects.requireNonNull(response);
        return response;
    }
}
