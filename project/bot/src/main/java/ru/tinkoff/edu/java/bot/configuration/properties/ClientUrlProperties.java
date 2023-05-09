package ru.tinkoff.edu.java.bot.configuration.properties;

import org.springframework.boot.context.properties.bind.ConstructorBinding;

public record ClientUrlProperties(
        String scrapperClientUrl
) {

    public static final String DEFAULT_SCRAPPER_API = "http://localhost:8082";

    public ClientUrlProperties() {
        this(null);
    }

    @ConstructorBinding
    public ClientUrlProperties {
    }

}
