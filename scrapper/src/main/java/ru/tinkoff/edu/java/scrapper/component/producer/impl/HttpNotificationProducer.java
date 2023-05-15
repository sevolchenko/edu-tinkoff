package ru.tinkoff.edu.java.scrapper.component.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.shared.bot.dto.request.LinkUpdateRequest;

@Slf4j
@RequiredArgsConstructor
public class HttpNotificationProducer implements INotificationProducer {

    private final ITgBotClient tgBotClient;

    @Override
    public void sendUpdate(LinkUpdateRequest linkUpdate) {

        log.info("Notifying about link {} chats {}. Event code: {}",
                linkUpdate.url(), linkUpdate.tgChatIds(), linkUpdate.event().getCode());

        tgBotClient.postUpdates(linkUpdate);
    }

}
