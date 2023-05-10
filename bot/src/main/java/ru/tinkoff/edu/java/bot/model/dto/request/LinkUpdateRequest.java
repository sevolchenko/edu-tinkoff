package ru.tinkoff.edu.java.bot.model.dto.request;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Long linkId,
                                URI url,
                                Integer eventCode,
                                List<Long> tgChatIds) {
}