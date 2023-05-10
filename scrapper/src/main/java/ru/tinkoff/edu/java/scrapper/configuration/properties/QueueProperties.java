package ru.tinkoff.edu.java.scrapper.configuration.properties;

public record QueueProperties(

        String name,
        String exchange,
        String routingKey

) {
}
