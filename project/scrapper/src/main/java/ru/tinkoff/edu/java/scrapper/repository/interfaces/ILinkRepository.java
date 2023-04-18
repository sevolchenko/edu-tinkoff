package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.SubscriptionRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.SubscriptionResponse;

import java.util.Collection;

@Repository
public interface ILinkRepository {

    Long add(SubscriptionRequest request); // returns link id

    SubscriptionResponse remove(SubscriptionRequest request);

    Collection<SubscriptionResponse> findAll(Long tgChatId);

}
