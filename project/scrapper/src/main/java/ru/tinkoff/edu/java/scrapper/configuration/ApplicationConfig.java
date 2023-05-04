package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.util.Optional;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull SchedulerConfiguration scheduler,
        @NotNull ClientUrlConfiguration client,
        @NotNull Duration linkCheckDelay,
        AccessType databaseAccessType,
        @NotNull Boolean useQueue) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull SchedulerConfiguration scheduler,
                             ClientUrlConfiguration client,
                             @NotNull Duration linkCheckDelay,
                             AccessType databaseAccessType,
                             Boolean useQueue) {
        this.scheduler = scheduler;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfiguration(null, null, null));
        this.linkCheckDelay = linkCheckDelay;
        this.databaseAccessType = Optional.ofNullable(databaseAccessType).orElse(AccessType.JDBC);
        this.useQueue = Optional.ofNullable(useQueue).orElse(false);
    }

    @Bean
    public String delay() {
        return scheduler().interval().toString();
    }

    @Bean
    public ClientUrlConfiguration clientUrlConfig() {
        return client;
    }

    @Bean
    public Duration linkCheckDelay() {
        return linkCheckDelay;
    }

}