package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.component.producer.impl.RabbitNotificationProducer;
import ru.tinkoff.edu.java.scrapper.configuration.properties.QueueProperties;

import java.util.List;

@Configuration
@ConditionalOnBean(RabbitNotificationProducer.class) // Чтобы очередь конфигурировалась только если она используется
public class RabbitMQConfiguration {

    private static String deadExchangeName(String exchange) {
        return exchange + ".dlx";
    }

    private static String deadLetterQueueName(String exchange) {
        return exchange + ".dlq";
    }

    @Bean
    public AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory,
                                         MessageConverter messageConverter) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    @Bean
    public Queue directQueue(QueueProperties queueProperties) {
        return QueueBuilder.durable(queueProperties.name())
                .withArgument("x-dead-letter-exchange", deadExchangeName(queueProperties.exchange()))
                .withArgument("x-dead-letter-routing-key", deadLetterQueueName(queueProperties.name()))
                .build();
    }

    @Bean
    public DirectExchange directExchange(QueueProperties queueProperties) {
        return new DirectExchange(queueProperties.exchange(), true, false);
    }


    @Bean
    public List<Binding> bindings(QueueProperties queueProperties, Queue directQueue, DirectExchange directExchange) {
        return List.of(
                BindingBuilder.bind(directQueue).to(directExchange).with(queueProperties.routingKey())
        );
    }
}
