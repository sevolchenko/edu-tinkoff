package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.TestScrapperClient;

@Configuration
public class TestConfiguration {

    @Bean
    @Primary
    public IScrapperClient scrapperClient() {
        return new TestScrapperClient();
    }


}
