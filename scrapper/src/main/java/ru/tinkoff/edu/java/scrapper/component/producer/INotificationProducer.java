package ru.tinkoff.edu.java.scrapper.component.producer;

import ru.tinkoff.edu.java.shared.bot.request.LinkUpdateRequest;

public interface INotificationProducer {

    void sendUpdate(LinkUpdateRequest linkUpdate);

}
