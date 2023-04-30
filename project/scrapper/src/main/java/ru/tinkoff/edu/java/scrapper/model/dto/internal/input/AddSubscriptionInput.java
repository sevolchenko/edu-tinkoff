package ru.tinkoff.edu.java.scrapper.model.dto.internal.input;

import java.time.OffsetDateTime;

public record AddSubscriptionInput(Long tgChatId, Long linkId, OffsetDateTime createdAt) {
}
