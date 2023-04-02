package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.impl.GitHubClient;
import ru.tinkoff.edu.java.scrapper.client.impl.StackOverflowClient;

import java.util.Optional;

@ConfigurationProperties(prefix = "app.client")
public record ClientConfiguration (
        String gitHubClientUrl,
        String stackOverflowClientUrl
) {

    private static final String GIT_HUB_API = "https://api.github.com";
    private static final String STACK_OVERFLOW_API = "https://api.stackexchange.com/2.3";

    @Bean
    public IGitHubClient gitHubClient() {
        return new GitHubClient(Optional.ofNullable(gitHubClientUrl).orElse(GIT_HUB_API));
    }

    @Bean
    public IStackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(Optional.ofNullable(stackOverflowClientUrl).orElse(STACK_OVERFLOW_API));
    }

}
