package ru.tinkoff.edu.java.scrapper.model.dto.internal.input;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;

import java.time.OffsetDateTime;

public record AddLinkInput(String url, ILinkState state, OffsetDateTime lastScannedAt, OffsetDateTime createdAt) {
}
