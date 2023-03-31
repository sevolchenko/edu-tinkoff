package ru.tinkoff.edu.java.scrapper.client;

import ru.tinkoff.edu.java.scrapper.client.dto.request.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.GitHubRepositoryResponse;

public interface IGitHubClient {

    GitHubRepositoryResponse fetchRepository(GitHubRepositoryRequest gitHubRepositoryRequest);

}
