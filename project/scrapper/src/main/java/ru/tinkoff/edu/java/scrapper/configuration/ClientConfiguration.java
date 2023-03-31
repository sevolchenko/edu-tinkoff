package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.client.impl.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.impl.StackOverflowClient;

@Validated
@Configuration
public class ClientConfiguration {

    @Bean
    public GitHubClient gitHubClient() {
        return GitHubClient.create();
    }

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return StackOverflowClient.create();
    }

}
