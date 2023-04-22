package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.ClientUrlConfig;

import java.util.Optional;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String botToken, ClientUrlConfig client) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull String botToken,
                             ClientUrlConfig client) {
        this.botToken = botToken;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfig(null));
    }

    @Bean(name = "botToken")
    public String botToken() {
        return botToken;
    }

    @Bean
    public ClientUrlConfig clientUrlConfig() {
        return client;
    }

}