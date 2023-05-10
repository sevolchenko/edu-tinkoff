package ru.tinkoff.edu.java.scrapper.model.dto.internal.input;

import java.time.OffsetDateTime;

public record AddTgChatInput(Long tgChatId, String username, OffsetDateTime registeredAt) {
}
