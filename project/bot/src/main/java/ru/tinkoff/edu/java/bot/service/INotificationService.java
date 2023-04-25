package ru.tinkoff.edu.java.bot.service;

import java.net.URI;
import java.util.List;

public interface INotificationService {

    void linkUpdateReceived(URI link, String description, List<Long> tgChatIds);

}
