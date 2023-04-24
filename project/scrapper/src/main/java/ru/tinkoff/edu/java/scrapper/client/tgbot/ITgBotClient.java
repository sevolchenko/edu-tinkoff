package ru.tinkoff.edu.java.scrapper.client.tgbot;

import ru.tinkoff.edu.java.scrapper.client.tgbot.dto.LinkUpdateRequest;

public interface ITgBotClient {

    void postUpdates(LinkUpdateRequest linkUpdateRequest);

}
