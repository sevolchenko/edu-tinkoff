package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.scrapper.configuration.properties.AccessType;
import ru.tinkoff.edu.java.scrapper.configuration.properties.ClientUrlProperties;
import ru.tinkoff.edu.java.scrapper.configuration.properties.QueueProperties;
import ru.tinkoff.edu.java.scrapper.configuration.properties.SchedulerProperties;
import ru.tinkoff.edu.java.scrapper.configuration.properties.mapping.PropertiesMapper;

import java.time.Duration;
import java.util.Optional;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(
        @NotNull SchedulerProperties scheduler,
        @NotNull ClientUrlProperties client,
        @NotNull Duration linkCheckDelay,
        AccessType databaseAccessType,
        @NotNull Boolean useQueue,
        QueueProperties queue
) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull SchedulerProperties scheduler,
                             ClientUrlProperties client,
                             @NotNull Duration linkCheckDelay,
                             AccessType databaseAccessType,
                             Boolean useQueue,
                             QueueProperties queue) {
        var propertiesMapper = Mappers.getMapper(PropertiesMapper.class);

        this.scheduler = scheduler;
        this.client = propertiesMapper.fillDefaults(Optional.ofNullable(client).orElse(new ClientUrlProperties()));
        this.linkCheckDelay = linkCheckDelay;
        this.databaseAccessType = Optional.ofNullable(databaseAccessType).orElse(AccessType.DEFAULT);
        this.useQueue = Optional.ofNullable(useQueue).orElse(false);
        this.queue = queue;
    }

    @Bean
    public String delay() {
        return scheduler().interval().toString();
    }

    @Bean
    public ClientUrlProperties clientUrlProperties() {
        return client;
    }

    @Bean
    public Duration linkCheckDelay() {
        return linkCheckDelay;
    }

    @Bean
    public QueueProperties queueProperties() {
        return queue;
    }

}