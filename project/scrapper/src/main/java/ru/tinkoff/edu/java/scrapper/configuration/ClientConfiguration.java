package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.github.impl.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.impl.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.impl.TgBotClient;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ClientUrlConfiguration clientUrlConfiguration;

    private static final String TG_BOT_API = "http://localhost:8081";

    private static final String GIT_HUB_API = "https://api.github.com";
    private static final String STACK_OVERFLOW_API = "https://api.stackexchange.com/2.3";

    @Bean
    public IGitHubClient gitHubClient() {
        return new GitHubClient(Optional.ofNullable(clientUrlConfiguration.gitHubClientUrl()).orElse(GIT_HUB_API));
    }

    @Bean
    public IStackOverflowClient stackOverflowClient() {
       return new StackOverflowClient(Optional.ofNullable(clientUrlConfiguration.stackOverflowClientUrl()).orElse(STACK_OVERFLOW_API));
    }

    @Bean
    public ITgBotClient tgBotClient() {
        return new TgBotClient(Optional.ofNullable(clientUrlConfiguration.tgBotClientUrl()).orElse(TG_BOT_API));
    }

}
