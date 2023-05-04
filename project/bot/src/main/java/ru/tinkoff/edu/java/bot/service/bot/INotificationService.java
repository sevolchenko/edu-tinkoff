package ru.tinkoff.edu.java.bot.service.bot;

import ru.tinkoff.edu.java.bot.model.dto.request.LinkEvent;

import java.net.URI;
import java.util.List;

public interface INotificationService {

    void linkUpdateReceived(URI link, LinkEvent event, List<Long> tgChatIds);

}
