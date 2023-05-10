package ru.tinkoff.edu.java.scrapper.configuration.properties;

public record ClientUrlProperties(
        String tgBotClientUrl,
        String gitHubClientUrl,
        String stackOverflowClientUrl) {
    public static final String DEFAULT_TG_BOT_API = "http://localhost:8081";
    public static final String DEFAULT_GIT_HUB_API = "https://api.github.com";
    public static final String DEFAULT_STACK_OVERFLOW_API = "https://api.stackexchange.com/2.3";

    public ClientUrlProperties() {
        this(null, null, null);
    }

}
