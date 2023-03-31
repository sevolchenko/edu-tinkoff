package ru.tinkoff.edu.java.scrapper.client.dto.response;

import java.util.List;

public record StackOverflowQuestionsResponse(List<StackOverflowQuestionResponse> items) {}
