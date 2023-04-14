package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.linkparser.LinkParser;

@Configuration
public class ServiceConfiguration {

    @Bean
    public LinkParser linkParser() {
        return new LinkParser();
    }

}
