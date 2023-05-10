package ru.tinkoff.edu.java.scrapper.client.github.dto;

public record GitHubRepositoryResponse(
        String fullName,
        GitHubLinkState state) {
}
