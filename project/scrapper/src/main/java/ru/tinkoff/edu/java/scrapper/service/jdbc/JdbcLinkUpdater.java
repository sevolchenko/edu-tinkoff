package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements ILinkUpdater {

    private final ILinkRepository linkRepository;

    private final ITgBotClient tgBotClient;
    private final IGitHubClient gitHubClient;
    private final IStackOverflowClient stackOverflowClient;

    @Override
    public void update() {

        var links = linkRepository.findUncheckedLinks();
        links.stream()
                .forEach(link -> {



                });
    }
}
