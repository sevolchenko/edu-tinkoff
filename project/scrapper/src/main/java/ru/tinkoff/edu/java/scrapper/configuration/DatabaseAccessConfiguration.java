package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkMapper;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatMapper;
import ru.tinkoff.edu.java.scrapper.model.mapping.TimeMapper;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcTgChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaTgChatService;

import java.time.Duration;

@Configuration
public class DatabaseAccessConfiguration {

    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
    public static class JdbcAccessConfiguration {

        @Bean
        public ILinkService linkService(
                JdbcLinkRepository linkRepository
        ) {
            return new JdbcLinkService(linkRepository);
        }

        @Bean
        public ITgChatService tgChatService(
                JdbcTgChatRepository tgChatRepository
        ) {
            return new JdbcTgChatService(tgChatRepository);
        }

        @Bean
        public ILinkUpdater linkUpdater(
                JdbcLinkRepository linkRepository,
                Duration linkCheckDelay,
                LinkProcessor linkProcessor,
                NotificationBroker notificationBroker
        ) {
            return new JdbcLinkUpdater(linkRepository, linkCheckDelay, linkProcessor, notificationBroker);
        }

    }

    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    public static class JpaAccessConfiguration {

        @Bean
        public ILinkService linkService(
                JpaLinkRepository linkRepository,
                JpaSubscriptionRepository subscriptionRepository,
                JpaTgChatRepository tgChatRepository,
                LinkMapper linkMapper
        ) {
            return new JpaLinkService(linkRepository, subscriptionRepository, tgChatRepository, linkMapper);
        }

        @Bean
        public ITgChatService tgChatService(
                JpaTgChatRepository tgChatRepository,
                TgChatMapper tgChatMapper
        ) {
            return new JpaTgChatService(tgChatRepository, tgChatMapper);
        }

        @Bean
        public ILinkUpdater linkUpdater(
                JpaLinkRepository linkRepository,
                Duration linkCheckDelay,
                LinkProcessor linkProcessor,
                NotificationBroker notificationBroker,
                TimeMapper timeMapper
        ) {
            return new JpaLinkUpdater(linkRepository, linkCheckDelay, linkProcessor, notificationBroker, timeMapper);
        }

    }

}
