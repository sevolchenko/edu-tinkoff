package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Slf4j
@Component
public class LinkUpdaterScheduler {

    @Scheduled(fixedDelayString = "#{@delay}")
    private void update() {
        log.info(String.format("Scheduled at %s", OffsetDateTime.now()));
    }

}
