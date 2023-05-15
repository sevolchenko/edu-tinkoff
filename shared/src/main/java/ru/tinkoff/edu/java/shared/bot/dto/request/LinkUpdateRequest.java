package ru.tinkoff.edu.java.shared.bot.dto.request;

import ru.tinkoff.edu.java.shared.scrapper.event.LinkEvent;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(

        Long linkId,

        URI url,

        LinkEvent event,

        List<Long> tgChatIds
) {
}
