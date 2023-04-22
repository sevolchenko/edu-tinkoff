package ru.tinkoff.edu.java.bot.model.dto.request;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Integer linkId,
                                URI url,
                                String description,
                                List<Integer> tgChatIds) {}