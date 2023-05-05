package ru.tinkoff.edu.java.scrapper.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

public record ClientUrlProperties(
        String tgBotClientUrl,
        String gitHubClientUrl,
        String stackOverflowClientUrl) {
    private static final String DEFAULT_TG_BOT_API = "http://localhost:8081";
    private static final String DEFAULT_GIT_HUB_API = "https://api.github.com";
    private static final String DEFAULT_STACK_OVERFLOW_API = "https://api.stackexchange.com/2.3";

    public static final ClientUrlProperties EMPTY = new ClientUrlProperties(null, null, null);

    @ConstructorBinding
    public ClientUrlProperties(ClientUrlProperties base) {
        this(
                Optional.ofNullable(base.tgBotClientUrl).orElse(DEFAULT_TG_BOT_API),
                Optional.ofNullable(base.gitHubClientUrl).orElse(DEFAULT_GIT_HUB_API),
                Optional.ofNullable(base.stackOverflowClientUrl).orElse(DEFAULT_STACK_OVERFLOW_API)
        );
    }

}
