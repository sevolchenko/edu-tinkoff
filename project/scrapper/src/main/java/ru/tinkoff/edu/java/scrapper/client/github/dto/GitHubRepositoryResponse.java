package ru.tinkoff.edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GitHubRepositoryResponse(
        @JsonProperty("full_name")
        String fullName,
        @JsonProperty("pushed_at")
        OffsetDateTime pushedAt) { }
