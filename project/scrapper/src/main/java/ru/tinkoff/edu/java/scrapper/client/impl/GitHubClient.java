package ru.tinkoff.edu.java.scrapper.client.impl;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.dto.request.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.GitHubRepositoryResponse;

import java.util.Objects;

public class GitHubClient implements IGitHubClient {

    private final WebClient webClient;

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
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
        return response;
    }
}
