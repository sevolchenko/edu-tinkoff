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
public record ApplicationConfig(@NotNull SchedulerConfiguration scheduler, @NotNull ClientUrlConfiguration client,
                                @NotNull Duration linkCheckDelay, AccessType databaseAccessType) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull SchedulerConfiguration scheduler,
                             ClientUrlConfiguration client,
                             @NotNull Duration linkCheckDelay,
                             AccessType databaseAccessType) {
        this.scheduler = scheduler;
        this.client = Optional.ofNullable(client).orElse(new ClientUrlConfiguration(null, null, null));
        this.linkCheckDelay = linkCheckDelay;
        this.databaseAccessType = Optional.ofNullable(databaseAccessType).orElse(AccessType.JDBC);
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