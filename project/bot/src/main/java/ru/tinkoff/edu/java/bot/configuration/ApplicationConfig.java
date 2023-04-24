package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String botToken, @NotNull ClientUrlConfiguration client) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull String botToken,
                             ClientUrlConfiguration client) {
        this.botToken = botToken;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfiguration(null));
    }

    @Bean(name = "botToken")
    public String botToken() {
        return botToken;
    }

    @Bean
    public ClientUrlConfiguration clientUrlConfig() {
        return client;
    }

}