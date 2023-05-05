package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.producer.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.net.URI;
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
    private final INotificationProducer notificationProducer;

    @Override
    public void update() {

        var links = linkRepository.findAllByLastScannedAtIsBefore(Instant.now(Clock.systemUTC()).minus(linkCheckDelay));

        log.info("Found {} unchecked links", links.size());

        links.forEach(link -> {

            log.info("Processing update link {}", link.getUrl());

            var newState = linkProcessor.getState(URI.create(link.getUrl()));

            var event = link.getState().compareTo(newState);

            if (event != null) {

                log.info("Event \"{}\" found for link {}",
                        event.getDescription(), link.getUrl());

                var tgChatIds = link.getSubscriptions().stream()
                        .map(subscription -> {
                            var tgChat = subscription.getSubscriptionId().getTgChat();
                            return tgChat.getTgChatId();
                        })
                        .toList();

                var update = new LinkUpdateRequest(link.getLinkId(),
                        URI.create(link.getUrl()), event.getCode(), tgChatIds);

                notificationProducer.sendUpdate(update);
            } else {
                log.info("No changes detected at link {}", link.getUrl());
            }

            link.setLastScannedAt(Instant.now());
            link.setState(newState);

        });


    }

}
