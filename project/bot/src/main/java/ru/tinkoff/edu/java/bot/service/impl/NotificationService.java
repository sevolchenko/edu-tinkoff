package ru.tinkoff.edu.java.bot.service.impl;

import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.bot.service.IBot;
import ru.tinkoff.edu.java.bot.service.INotificationService;
import ru.tinkoff.edu.java.bot.service.text.TextProvider.NotificationTextProvider;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class NotificationService implements INotificationService {

    private final IBot bot;
    @Override
    public void linkUpdateReceived(URI link, String description, List<Long> tgChatIds) {
        log.info("Received link {} update: {}", link, description);

        tgChatIds.forEach(tgChatId -> {
            var text = NotificationTextProvider.buildLinksListText(link.toString());
            bot.execute(new SendMessage(tgChatId, text));
        });
    }

}
