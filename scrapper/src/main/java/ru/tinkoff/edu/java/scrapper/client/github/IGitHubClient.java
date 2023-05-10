package ru.tinkoff.edu.java.scrapper.client.github;

import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;

public interface IGitHubClient {

    GitHubRepositoryResponse fetchRepository(GitHubRepositoryRequest gitHubRepositoryRequest);

}
