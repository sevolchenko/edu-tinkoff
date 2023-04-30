package ru.tinkoff.edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class GitHubRepositoryAPIResponse{

        @JsonProperty("full_name")
        private String fullName;
        @JsonProperty("pushed_at")
        private OffsetDateTime pushedAt;
        private Integer branchesCount;
}
