package ru.tinkoff.edu.java.scrapper.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ISubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;
import ru.tinkoff.edu.java.shared.bot.request.LinkUpdateRequest;

import java.net.URI;
import java.time.Duration;
import java.time.OffsetDateTime;

@Slf4j
@Transactional
@RequiredArgsConstructor
public class LinkUpdater implements ILinkUpdater {

    private final ILinkRepository linkRepository;
    private final ISubscriptionRepository subscriptionRepository;

    private final Duration linkCheckDelay;

    private final LinkProcessor linkProcessor;
    private final INotificationProducer notificationProducer;

    @Override
    public void update() {

        var links = linkRepository.findAllByLastScannedAtIsBefore(OffsetDateTime.now().minus(linkCheckDelay));

        log.info("Found {} unchecked links", links.size());

        links.forEach(link -> {

            log.info("Processing update link {}", link.getUrl());

            var newState = linkProcessor.getState(URI.create(link.getUrl()));

            var event = link.getState().compareTo(newState);

            if (event != null) {

                log.info("Event \"{}\" found for link {}",
                        event.getDescription(), link.getUrl());

                var tgChatIds = subscriptionRepository.findAllByLinkId(link.getLinkId()).stream()
                        .map(SubscriptionOutput::getTgChatId)
                        .toList();

                var update = new LinkUpdateRequest(link.getLinkId(),
                        URI.create(link.getUrl()), event, tgChatIds);

                notificationProducer.sendUpdate(update);
            }

            linkRepository.updateLastScannedAt(link.getLinkId(), newState, OffsetDateTime.now());

        });


    }
}
