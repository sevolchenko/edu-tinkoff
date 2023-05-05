package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.configuration.properties.ClientUrlProperties;
import ru.tinkoff.edu.java.bot.configuration.properties.QueueProperties;

import java.util.Optional;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String botToken,
                                @NotNull ClientUrlProperties client,
                                @NotNull QueueProperties queue) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull String botToken,
                             ClientUrlProperties client,
                             @NotNull QueueProperties queue) {
        this.botToken = botToken;
        this.client = new ClientUrlProperties(Optional.ofNullable(client).orElse(ClientUrlProperties.EMPTY));
        this.queue = queue;
    }

    @Bean(name = "botToken")
    public String botToken() {
        return botToken;
    }

    @Bean
    public ClientUrlProperties clientUrlProperties() {
        return client;
    }

    @Bean
    public QueueProperties queueProperties() {
        return queue;
    }

}