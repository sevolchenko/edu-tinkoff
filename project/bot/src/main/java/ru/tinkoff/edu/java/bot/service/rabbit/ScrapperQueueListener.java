package ru.tinkoff.edu.java.bot.service.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkEvent;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.service.bot.INotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "directQueue")
public class ScrapperQueueListener {

    private final INotificationService notificationService;

    @RabbitHandler
    private void received(LinkUpdateRequest linkUpdateRequest) {

        notificationService.linkUpdateReceived(linkUpdateRequest.url(),
                LinkEvent.get(linkUpdateRequest.eventCode()), linkUpdateRequest.tgChatIds());


    }

}
