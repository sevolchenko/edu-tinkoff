package ru.tinkoff.edu.java.bot.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.ClientUrlConfig;
import ru.tinkoff.edu.java.bot.client.impl.ScrapperClient;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
public class ClientConfiguration {

    private final ClientUrlConfig clientUrlConfig;

    private static final String SCRAPPER_API = "http://localhost:8082";

    @Bean
    public IScrapperClient scrapperClient() {
        return new ScrapperClient(Optional.ofNullable(clientUrlConfig.scrapperClientUrl()).orElse(SCRAPPER_API));
    }

}
