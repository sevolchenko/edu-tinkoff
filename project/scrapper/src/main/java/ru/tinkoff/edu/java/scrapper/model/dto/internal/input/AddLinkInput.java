package ru.tinkoff.edu.java.scrapper.model.dto.internal.input;

import java.time.OffsetDateTime;

public record AddLinkInput(String url, OffsetDateTime lastScannedAt, OffsetDateTime createdAt) {
}
