package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.LinkProcessInput;
import ru.tinkoff.edu.java.scrapper.model.mapping.TimeMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@RequiredArgsConstructor
@Slf4j
public class JpaLinkUpdater implements ILinkUpdater {

    private final JpaLinkRepository linkRepository;

    private final Duration linkCheckDelay;

    private final LinkProcessor linkProcessor;
    private final NotificationBroker notificationBroker;

    private final TimeMapper timeMapper;

    @Override
    @Transactional
    public void update() {

        var links = linkRepository.findAllByLastScannedAtIsBefore(Instant.now(Clock.systemUTC()).minus(linkCheckDelay));

        log.info("Found {} unchecked links", links.size());

        links.forEach(link -> {

            log.info("Processing update link {}", link.getUrl());

            var linkProcessInput = new LinkProcessInput(timeMapper.map(link.getLastScannedAt()));

            if (linkProcessor.processLink(link.getUrl(), linkProcessInput)) {

                var tgChatIds = link.getSubscriptions().stream()
                        .map(subscription -> {
                            var tgChat = subscription.getSubscriptionId().getTgChat();
                            return tgChat.getTgChatId();
                        })
                        .toList();

                notificationBroker.sendUpdate(link.getLinkId(), link.getUrl(), "Link updated", tgChatIds);
            } else {
                log.info("No changes detected at link {}", link.getUrl());
            }

            link.setLastScannedAt(Instant.now());

        });


    }

}
