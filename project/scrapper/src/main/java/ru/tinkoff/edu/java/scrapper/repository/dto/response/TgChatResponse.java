package ru.tinkoff.edu.java.scrapper.repository.dto.response;

import java.time.OffsetDateTime;

public record TgChatResponse(Long tgChatId, String username, OffsetDateTime registeredAt) {
}
