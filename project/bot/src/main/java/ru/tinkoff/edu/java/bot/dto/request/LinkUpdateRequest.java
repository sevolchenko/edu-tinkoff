package ru.tinkoff.edu.java.bot.dto.request;

import org.springframework.lang.NonNull;

import java.net.URI;

public record LinkUpdateRequest(@NonNull Integer id, @NonNull URI url, @NonNull String description, @NonNull Integer[] tgChatIds) {}
