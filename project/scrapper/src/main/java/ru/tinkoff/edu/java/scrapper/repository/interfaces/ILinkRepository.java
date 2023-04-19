package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.repository.dto.request.SubscriptionRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.LinkResponse;

import java.util.Collection;

public interface ILinkRepository {

    Long add(SubscriptionRequest request); // returns link id

    LinkResponse remove(SubscriptionRequest request);

    Collection<LinkResponse> findAll(Long tgChatId);

    LinkResponse findById(SubscriptionRequest request);

}
