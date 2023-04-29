package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionIdInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddSubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionIdOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;

import java.util.List;

public interface ISubscriptionRepository {

    SubscriptionIdOutput save(AddSubscriptionInput subscription);

    SubscriptionOutput remove(SubscriptionIdInput subscriptionId);

    List<SubscriptionOutput> findAll();

    List<SubscriptionOutput> findAllByLinkId(Long linkId);

    SubscriptionOutput findBySubscriptionId(SubscriptionIdInput subscriptionId);

}
