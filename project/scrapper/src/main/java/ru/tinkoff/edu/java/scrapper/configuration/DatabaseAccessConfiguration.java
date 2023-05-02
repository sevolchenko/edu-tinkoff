package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.component.broker.NotificationBroker;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkOutputMapper;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqTgChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.impl.LinkService;
import ru.tinkoff.edu.java.scrapper.service.impl.LinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.impl.TgChatService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;
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
                JdbcLinkRepository linkRepository,
                JdbcSubscriptionRepository subscriptionRepository,
                JdbcTgChatRepository tgChatRepository,
                LinkProcessor linkProcessor
        ) {
            return new LinkService(linkRepository, subscriptionRepository, tgChatRepository, linkProcessor);
        }

        @Bean
        public ITgChatService tgChatService(
                JdbcTgChatRepository tgChatRepository
        ) {
            return new TgChatService(tgChatRepository);
        }

        @Bean
        public ILinkUpdater linkUpdater(
                JdbcLinkRepository linkRepository,
                JdbcSubscriptionRepository subscriptionRepository,
                Duration linkCheckDelay,
                LinkProcessor linkProcessor,
                NotificationBroker notificationBroker
        ) {
            return new LinkUpdater(linkRepository, subscriptionRepository,
                    linkCheckDelay, linkProcessor, notificationBroker);
        }

    }

    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jooq")
    public static class JooqAccessConfiguration {

        @Bean
        public ILinkService linkService(
                JooqLinkRepository linkRepository,
                JooqSubscriptionRepository subscriptionRepository,
                JooqTgChatRepository tgChatRepository,
                LinkProcessor linkProcessor
        ) {
            return new LinkService(linkRepository, subscriptionRepository, tgChatRepository, linkProcessor);
        }

        @Bean
        public ITgChatService tgChatService(
                JooqTgChatRepository tgChatRepository
        ) {
            return new TgChatService(tgChatRepository);
        }

        @Bean
        public ILinkUpdater linkUpdater(
                JooqLinkRepository linkRepository,
                JooqSubscriptionRepository subscriptionRepository,
                Duration linkCheckDelay,
                LinkProcessor linkProcessor,
                NotificationBroker notificationBroker
        ) {
            return new LinkUpdater(linkRepository, subscriptionRepository,
                    linkCheckDelay, linkProcessor, notificationBroker);
        }

    }

    @ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
    public static class JpaAccessConfiguration {

        @Bean
        public ILinkService linkService(
                JpaLinkRepository linkRepository,
                JpaSubscriptionRepository subscriptionRepository,
                JpaTgChatRepository tgChatRepository,
                LinkProcessor linkProcessor,
                LinkOutputMapper mapper
        ) {
            return new JpaLinkService(linkRepository, subscriptionRepository,
                    tgChatRepository, linkProcessor, mapper);
        }

        @Bean
        public ITgChatService tgChatService(
                JpaTgChatRepository tgChatRepository,
                TgChatOutputMapper mapper
        ) {
            return new JpaTgChatService(tgChatRepository, mapper);
        }

        @Bean
        public ILinkUpdater linkUpdater(
                JpaLinkRepository linkRepository,
                Duration linkCheckDelay,
                LinkProcessor linkProcessor,
                NotificationBroker notificationBroker
        ) {
            return new JpaLinkUpdater(linkRepository, linkCheckDelay, linkProcessor, notificationBroker);
        }

    }

}
