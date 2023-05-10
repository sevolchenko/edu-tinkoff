package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto;

import java.util.List;

public record StackOverflowQuestionListResponse(
        List<StackOverflowAPIResponse> items
) {
}
