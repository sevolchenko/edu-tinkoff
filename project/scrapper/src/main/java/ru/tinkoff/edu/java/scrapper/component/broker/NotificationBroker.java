package ru.tinkoff.edu.java.scrapper.component.broker;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.dto.LinkUpdateRequest;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationBroker {

    private final ITgBotClient tgBotClient;

    public void sendUpdate(Long linkId, String url, String description, List<Long> tgChatIds) {
        tgBotClient.postUpdates(new LinkUpdateRequest(linkId, URI.create(url), description, tgChatIds));
    }

}
