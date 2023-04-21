package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.client.dto.ClientUrlConfig;
import ru.tinkoff.edu.java.scrapper.scheduler.SchedulerConfig;

import java.time.Duration;
import java.util.Optional;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull SchedulerConfig scheduler, @NotNull ClientUrlConfig client, @NotNull Duration linkUpdatePeriod) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull SchedulerConfig scheduler,
                             ClientUrlConfig client,
                             @NotNull Duration linkUpdatePeriod) {
        this.scheduler = scheduler;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfig(null, null, null));
        this.linkUpdatePeriod = linkUpdatePeriod;
    }

    @Bean
    public String delay() {
        return scheduler().interval().toString();
    }

    @Bean
    public ClientUrlConfig clientUrlConfig() {
        return client;
    }

    @Bean
    public Duration linkUpdatePeriod() {
        return linkUpdatePeriod;
    }

}