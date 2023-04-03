package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.client.dto.ClientUrlConfig;
import ru.tinkoff.edu.java.scrapper.scheduler.Scheduler;

import java.util.Optional;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull Scheduler scheduler, @NotNull ClientUrlConfig client) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull Scheduler scheduler,
                             ClientUrlConfig client) {
        this.scheduler = scheduler;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfig(null, null));
    }

    @Bean
    public String delay() {
        return scheduler().interval().toString();
    }

    @Bean
    public ClientUrlConfig clientUrlConfig() {
        return client;
    }

}