package ru.tinkoff.edu.java.scrapper.client.tgbot;

import ru.tinkoff.edu.java.shared.bot.dto.request.LinkUpdateRequest;

public interface ITgBotClient {

    void postUpdates(LinkUpdateRequest linkUpdateRequest);

}
