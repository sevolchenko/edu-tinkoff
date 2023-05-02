package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
@Transactional
public class JpaLinkUpdater implements ILinkUpdater {

    private final JpaLinkRepository linkRepository;

    private final Duration linkCheckDelay;

    private final LinkProcessor linkProcessor;
    private final NotificationBroker notificationBroker;

    @Override
    public void update() {

        var links = linkRepository.findAllByLastScannedAtIsBefore(Instant.now(Clock.systemUTC()).minus(linkCheckDelay));

        log.info("Found {} unchecked links", links.size());

        links.forEach(link -> {

            log.info("Processing update link {}", link.getUrl());

            var output = linkProcessor.processLink(link.getUrl(), link.getState());

            if (output.event() != null) {

                var tgChatIds = link.getSubscriptions().stream()
                        .map(subscription -> {
                            var tgChat = subscription.getSubscriptionId().getTgChat();
                            return tgChat.getTgChatId();
                        })
                        .toList();

                notificationBroker.sendUpdate(link.getLinkId(), link.getUrl(), output.event(), tgChatIds);
            } else {
                log.info("No changes detected at link {}", link.getUrl());
            }

            link.setLastScannedAt(Instant.now());
            link.setState(output.newState());

        });


    }

}
