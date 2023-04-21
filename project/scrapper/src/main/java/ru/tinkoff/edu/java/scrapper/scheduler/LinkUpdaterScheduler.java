package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.client.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Comparator;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final ILinkUpdater linkUpdater;
    private final ILinkService linkService;

    private final ITgBotClient tgBotClient;
    private final IGitHubClient gitHubClient;
    private final IStackOverflowClient stackOverflowClient;

    private final Duration linkUpdatePeriod;

    @Scheduled(fixedDelayString = "#{@delay}")
    private void update() {
        log.info("Scheduler performed action");

        var links = linkService.listAll();

        links.stream()
                .filter(link -> Duration.between(link.getLastScannedAt(), OffsetDateTime.now()).compareTo(linkUpdatePeriod) > 0)
                .forEach(link -> {


                    linkUpdater.update(link.getLinkId());
                });
    }

}
