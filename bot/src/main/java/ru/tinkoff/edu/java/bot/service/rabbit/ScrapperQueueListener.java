package ru.tinkoff.edu.java.bot.service.rabbit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.service.bot.INotificationService;
import ru.tinkoff.edu.java.shared.bot.dto.request.LinkUpdateRequest;

@Slf4j
@Component
@RequiredArgsConstructor
@RabbitListener(queues = "${app.queue.name}")
public class ScrapperQueueListener {

    private final INotificationService notificationService;

    @RabbitHandler
    public void received(LinkUpdateRequest linkUpdateRequest) {

        notificationService.linkUpdateReceived(linkUpdateRequest.url(),
                linkUpdateRequest.event(), linkUpdateRequest.tgChatIds());

    }

}
