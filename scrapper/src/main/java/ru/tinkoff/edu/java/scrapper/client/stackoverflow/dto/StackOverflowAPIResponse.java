package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowAPIResponse(

        String title,
        URI link,
        @JsonProperty("last_activity_date")
        OffsetDateTime lastActivityDate,

        @JsonProperty("answer_count")
        Integer answerCount
) {
}
