package ru.tinkoff.edu.java.bot.configuration;

import jakarta.validation.constraints.NotNull;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import ru.tinkoff.edu.java.bot.configuration.properties.ClientUrlProperties;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.configuration.properties.QueueProperties;
import ru.tinkoff.edu.java.bot.configuration.properties.mapping.PropertiesMapper;

import java.util.Optional;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String botToken,
                                @NotNull MessageTemplates messageTemplates,
                                @NotNull ClientUrlProperties client,
                                @NotNull
                                QueueProperties queue) {

    @ConstructorBinding
    public ApplicationConfig(@NotNull String botToken,
                             MessageTemplates messageTemplates,
                             ClientUrlProperties client,
                             @NotNull QueueProperties queue) {
        var propertiesMapper = Mappers.getMapper(PropertiesMapper.class);

        this.botToken = botToken;
        this.messageTemplates = propertiesMapper.fillDefaults(
            Optional.ofNullable(messageTemplates).orElse(new MessageTemplates()));
        this.client = propertiesMapper.fillDefaults(Optional.ofNullable(client).orElse(new ClientUrlProperties()));
        this.queue = queue;
    }

    @Bean(name = "botToken")
    public String botToken() {
        return botToken;
    }

    @Bean
    public MessageTemplates messageTemplates() {
        return messageTemplates;
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
