package ru.tinkoff.edu.java.scrapper.client.github.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryAPIResponse;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.client.github.dto.mapping.GitHubResponseMapper;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundLinkException;

public class GitHubClient implements IGitHubClient {

    private final WebClient webClient;

    @Autowired
    private GitHubResponseMapper mapper;

    public GitHubClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public GitHubRepositoryResponse fetchRepository(GitHubRepositoryRequest gitHubRepositoryRequest) {
        String path = "/repos/{owner}/{repo}";
        var apiResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(gitHubRepositoryRequest.owner(), gitHubRepositoryRequest.repository()))
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals,
                        (response) -> {
                            throw new NotFoundLinkException(
                                    String.format("GitHub repo %s not found for user %s",
                                            gitHubRepositoryRequest.repository(),
                                            gitHubRepositoryRequest.owner())
                            );
                        })
                .bodyToMono(GitHubRepositoryAPIResponse.class)
                .block();

        Integer branchesCount = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path + "/branches")
                        .build(gitHubRepositoryRequest.owner(), gitHubRepositoryRequest.repository()))
                .retrieve()
                .bodyToMono(Object[].class)
                .block()
                .length;

        apiResponse.setBranchesCount(branchesCount);

        return mapper.from(apiResponse);
    }
}
