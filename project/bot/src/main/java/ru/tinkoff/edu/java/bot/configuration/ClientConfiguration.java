package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.scrapper.impl.ScrapperClient;
import ru.tinkoff.edu.java.bot.configuration.properties.ClientUrlProperties;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ClientUrlProperties clientUrlProperties;

    @Bean
    public IScrapperClient scrapperClient() {
        return new ScrapperClient(clientUrlProperties.scrapperClientUrl());
    }

}
