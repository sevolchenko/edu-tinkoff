package ru.tinkoff.edu.java.scrapper.component.producer.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.component.producer.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;

@Component
@Slf4j
@RequiredArgsConstructor
public class RabbitNotificationProducer implements INotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendUpdate(LinkUpdateRequest linkUpdate) {

        log.info("Notifying about link {} chats {}. Event code: {}",
                linkUpdate.url(), linkUpdate.tgChatIds(), linkUpdate.eventCode());

           rabbitTemplate.convertAndSend(
                "directExchange", "directRoutingKey", linkUpdate);
    }

}
