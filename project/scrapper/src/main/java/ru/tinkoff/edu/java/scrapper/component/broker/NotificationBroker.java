package ru.tinkoff.edu.java.scrapper.component.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkEvent;

import java.net.URI;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationBroker {

    private final ITgBotClient tgBotClient;

    public void sendUpdate(Long linkId, String url, LinkEvent event, List<Long> tgChatIds) {

        log.info("Notifying about link {} chats {}. Event: {}", url, tgChatIds, event.getDescription());

        tgBotClient.postUpdates(new LinkUpdateRequest(linkId, URI.create(url), event.getCode(), tgChatIds));
    }

}
