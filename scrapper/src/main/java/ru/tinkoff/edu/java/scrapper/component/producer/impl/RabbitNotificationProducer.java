package ru.tinkoff.edu.java.scrapper.component.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.scrapper.configuration.properties.QueueProperties;
import ru.tinkoff.edu.java.shared.bot.request.LinkUpdateRequest;

@Slf4j
@RequiredArgsConstructor
public class RabbitNotificationProducer implements INotificationProducer {

    private final RabbitTemplate rabbitTemplate;
    private final QueueProperties queue;

    @Override
    public void sendUpdate(LinkUpdateRequest linkUpdate) {

        log.info("Notifying about link {} chats {}. Event code: {}",
                linkUpdate.url(), linkUpdate.tgChatIds(), linkUpdate.event().getCode());

        rabbitTemplate.convertAndSend(
                queue.exchange(), queue.routingKey(), linkUpdate);
    }

}
