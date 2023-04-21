package ru.tinkoff.edu.java.scrapper.client;

import ru.tinkoff.edu.java.scrapper.client.dto.request.LinkUpdateRequest;

public interface ITgBotClient {

    void postUpdates(LinkUpdateRequest linkUpdateRequest);

}
