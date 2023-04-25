package ru.tinkoff.edu.java.scrapper.component.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final ILinkUpdater linkUpdater;

    @Scheduled(fixedDelayString = "#{@delay}")
    private void update() {
        log.info("Checking for links updates");

//        linkUpdater.update();
    }

}
