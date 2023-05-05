package ru.tinkoff.edu.java.bot.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

import java.util.Optional;

public record ClientUrlProperties(String scrapperClientUrl) {

    private static final String DEFAULT_SCRAPPER_API = "http://localhost:8082";

    public static final ClientUrlProperties EMPTY = new ClientUrlProperties((String) null);

    @ConstructorBinding
    public ClientUrlProperties(ClientUrlProperties base) {
        this(
                Optional.ofNullable(base.scrapperClientUrl).orElse(DEFAULT_SCRAPPER_API)
        );
    }

}
