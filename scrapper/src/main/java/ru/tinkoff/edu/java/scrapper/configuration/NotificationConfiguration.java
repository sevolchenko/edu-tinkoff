package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.scrapper.component.producer.impl.HttpNotificationProducer;
import ru.tinkoff.edu.java.scrapper.component.producer.impl.RabbitNotificationProducer;
import ru.tinkoff.edu.java.scrapper.configuration.properties.QueueProperties;

@Configuration
public class NotificationConfiguration {

    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "false")
    public static class WithHttp {

        @Bean
        public INotificationProducer notificationProducer(ITgBotClient tgBotClient) {
            return new HttpNotificationProducer(tgBotClient);
        }

    }

    @ConditionalOnProperty(prefix = "app", name = "use-queue", havingValue = "true")
    public static class WithQueue {
        @Bean
        public INotificationProducer notificationProducer(RabbitTemplate rabbitTemplate,
                                                          QueueProperties queue) {
            return new RabbitNotificationProducer(rabbitTemplate, queue);
        }

    }

}
