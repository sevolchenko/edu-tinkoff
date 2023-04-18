package ru.tinkoff.edu.java.scrapper.repository.dto.response;

import java.time.OffsetDateTime;

public record LinkResponse(Long linkId, String url, OffsetDateTime scannedAt, OffsetDateTime createdAt) {
}
