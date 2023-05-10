package ru.tinkoff.edu.java.bot.configuration.properties;

public record QueueProperties(

        String name,
        String exchange,
        String routingKey

) {
}
