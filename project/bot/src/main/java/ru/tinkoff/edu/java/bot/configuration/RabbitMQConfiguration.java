package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.amqp.core.*;
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
import ru.tinkoff.edu.java.bot.model.dto.request.LinkUpdateRequest;

import java.util.HashMap;
import java.util.List;
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
    public ClassMapper classMapper(){
        Map<String, Class<?>> mappings = new HashMap<>();
        mappings.put("ru.tinkoff.edu.java.scrapper.component.producer.dto.LinkUpdateRequest", LinkUpdateRequest.class);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setTrustedPackages("ru.tinkoff.edu.java.scrapper.component.producer.properties.*");
        classMapper.setIdClassMapping(mappings);
        return classMapper;
    }

    @Bean
    public MessageConverter jsonMessageConverter(ClassMapper classMapper){
        Jackson2JsonMessageConverter jsonConverter=new Jackson2JsonMessageConverter();
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
                .withArgument("x-dead-letter-exchange", "")
                .withArgument("x-dead-letter-routing-key", deadLetterQueueName(queueProperties.name()))
                .build();
    }



    @Bean
    public List<Binding> bindings(QueueProperties queueProperties, Queue directQueue, DirectExchange directExchange) {
        return List.of(
                BindingBuilder.bind(directQueue).to(directExchange).with(queueProperties.routingKey())
        );
    }

}
