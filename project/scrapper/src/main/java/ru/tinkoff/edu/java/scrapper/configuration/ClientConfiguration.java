package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.impl.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.impl.StackOverflowClient;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ClientUrlConfiguration clientUrlConfiguration;

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

}
