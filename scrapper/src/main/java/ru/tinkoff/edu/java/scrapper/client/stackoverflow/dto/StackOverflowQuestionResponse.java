package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto;

import java.net.URI;

public record StackOverflowQuestionResponse(
        String title,
        URI link,
        StackOverflowLinkState state
) {
}
