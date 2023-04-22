package ru.tinkoff.edu.java.scrapper.client.github.impl;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;

public class GitHubClient implements IGitHubClient {

    private final WebClient webClient;

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public GitHubRepositoryResponse fetchRepository(GitHubRepositoryRequest gitHubRepositoryRequest) {
        String path = "/repos/{owner}/{repo}";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(gitHubRepositoryRequest.owner(), gitHubRepositoryRequest.repository()))
                .retrieve()
                .bodyToMono(GitHubRepositoryResponse.class)
                .block();
    }
}
