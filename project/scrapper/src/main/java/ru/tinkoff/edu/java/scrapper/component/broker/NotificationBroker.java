package ru.tinkoff.edu.java.scrapper.component.broker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.dto.LinkUpdateRequest;

import java.net.URI;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationBroker {

    private final ITgBotClient tgBotClient;

    public void sendUpdate(Long linkId, String url, String description, List<Long> tgChatIds) {

        log.info("Notifying about link {} chats {}", url, tgChatIds);

        tgBotClient.postUpdates(new LinkUpdateRequest(linkId, URI.create(url), description, tgChatIds));
    }

}
