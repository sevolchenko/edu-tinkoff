package ru.tinkoff.edu.java.bot.service.bot.impl;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.component.textprovider.NotificationTextProvider;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkEvent;
import ru.tinkoff.edu.java.bot.service.bot.IBot;
import ru.tinkoff.edu.java.bot.service.bot.INotificationService;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements INotificationService {

    private final IBot bot;
    private final NotificationTextProvider notificationTextProvider;

    @Override
    public void linkUpdateReceived(URI link, LinkEvent event, List<Long> tgChatIds) {
        log.info("Received link {} update: {}", link, event.getDescription());

        var text = notificationTextProvider.getNotificationText(event, link.toString());
        tgChatIds.forEach(tgChatId -> bot.execute(new SendMessage(tgChatId, text)));
    }

}
