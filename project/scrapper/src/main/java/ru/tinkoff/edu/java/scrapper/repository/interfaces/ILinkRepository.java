package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkWithChatsOutput;

import java.time.Duration;
import java.util.Collection;

public interface ILinkRepository {

    Long add(SubscriptionInput input); // returns link id

    LinkOutput remove(SubscriptionInput input);

    Collection<LinkWithChatsOutput> findUncheckedLinks(Duration linkCheckDelay);

    void updateLastScanTime(Long linkId);

    Collection<LinkOutput> findAll(Long tgChatId);

    LinkOutput findById(SubscriptionInput input);

}
