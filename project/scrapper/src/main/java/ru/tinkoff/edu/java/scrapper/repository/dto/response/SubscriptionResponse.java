package ru.tinkoff.edu.java.scrapper.repository.dto.response;

import java.time.OffsetDateTime;

public record SubscriptionResponse(Long tgChatId, LinkResponse link, OffsetDateTime createdAt) {
}
