package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;

import java.util.Collection;

public interface ILinkRepository {

    Long add(SubscriptionInput request); // returns link id

    LinkOutput remove(SubscriptionInput request);

    Collection<LinkOutput> findUncheckedLinks();

    Collection<LinkOutput> findAll(Long tgChatId);

    LinkOutput findById(SubscriptionInput request);

}
