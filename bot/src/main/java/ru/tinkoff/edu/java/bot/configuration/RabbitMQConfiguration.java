package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.configuration.properties.QueueProperties;
import ru.tinkoff.edu.java.shared.bot.dto.request.LinkUpdateRequest;

import java.util.HashMap;
import java.util.Map;

@Configuration
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
    public ClassMapper classMapper() {
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.shared.bot.request.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.shared.bot.dto.request.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper) {
        Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
        jsonConverter.setClassMapper(classMapper);
        return jsonConverter;
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
                .withArgument("x-dead-letter-routing-key", deadLetterQueueName(queueProperties.routingKey()))
                .build();
    }

    @Bean
    public Queue deadLetterQueue(QueueProperties queueProperties) {
        return QueueBuilder.durable(deadLetterQueueName(queueProperties.name())).build();
    }

    @Bean
    public DirectExchange directExchange(QueueProperties queueProperties) {
        return new DirectExchange(queueProperties.exchange(), true, false);
    }

    @Bean
    public DirectExchange deadLetterExchange(QueueProperties queueProperties) {
        return new DirectExchange(deadExchangeName(queueProperties.exchange()));
    }

    @Bean
    public Binding directBinding(QueueProperties queueProperties,
        Queue directQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(directQueue).to(directExchange)
            .with(queueProperties.routingKey());
    }

    @Bean
    public Binding dlqBinding(QueueProperties queueProperties,
        Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange)
            .with(deadLetterQueueName(queueProperties.routingKey()));
    }

}
