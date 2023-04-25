package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.LinkProcessInput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.time.Duration;

@Service
@Slf4j
@RequiredArgsConstructor
public class JdbcLinkUpdater implements ILinkUpdater {

    private final ILinkRepository linkRepository;

    private final Duration linkCheckDelay;

    private final LinkProcessor linkProcessor;
    private final NotificationBroker notificationBroker;

    @Override
    public void update() {

        var links = linkRepository.findUncheckedLinks(linkCheckDelay);

        log.info("Found {} unchecked links", links.size());

        links.stream()
                .forEach(link -> {

                    log.info("Processing update link {}", link.url());

                    var linkProcessInput = new LinkProcessInput(link.lastScannedAt());

                    if (linkProcessor.processLink(link.url(), linkProcessInput)) {
                        notificationBroker.sendUpdate(link.linkId(), link.url(), "Link updated", link.tgChatIds());
                    }

                    linkRepository.updateLastScanTime(link.linkId());

                });


    }
}
