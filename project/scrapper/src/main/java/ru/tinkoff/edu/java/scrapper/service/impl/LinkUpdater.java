package ru.tinkoff.edu.java.scrapper.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ISubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.time.Duration;
import java.time.OffsetDateTime;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LinkUpdater implements ILinkUpdater {

    private final ILinkRepository linkRepository;
    private final ISubscriptionRepository subscriptionRepository;

    private final Duration linkCheckDelay;

    private final LinkProcessor linkProcessor;
    private final NotificationBroker notificationBroker;

    @Override
    public void update() {

        var links = linkRepository.findAllByLastScannedAtIsBefore(OffsetDateTime.now().minus(linkCheckDelay));

        log.info("Found {} unchecked links", links.size());

        links.forEach(link -> {

            log.info("Processing update link {}", link.getUrl());

            var output = linkProcessor.processLink(link.getUrl(), link.getState());

            if (output.event() != null) {

                var tgChatIds = subscriptionRepository.findAllByLinkId(link.getLinkId()).stream()
                        .map(SubscriptionOutput::getTgChatId)
                        .toList();

                notificationBroker.sendUpdate(link.getLinkId(), link.getUrl(), output.event(), tgChatIds);
            }

            linkRepository.updateLastScannedAt(link.getLinkId(), output.newState(), OffsetDateTime.now());

        });


    }
}
