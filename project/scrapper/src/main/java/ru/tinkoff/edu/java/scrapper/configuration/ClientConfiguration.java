package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.impl.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.impl.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.impl.TgBotClient;
import ru.tinkoff.edu.java.scrapper.configuration.properties.ClientUrlProperties;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ClientUrlProperties clientUrlProperties;

    @Bean
    public IGitHubClient gitHubClient() {
        return new GitHubClient(clientUrlProperties.gitHubClientUrl());
    }

    @Bean
    public IStackOverflowClient stackOverflowClient() {
       return new StackOverflowClient(clientUrlProperties.stackOverflowClientUrl());
    }

    @Bean
    public ITgBotClient tgBotClient() {
        return new TgBotClient(clientUrlProperties.tgBotClientUrl());
    }

}
