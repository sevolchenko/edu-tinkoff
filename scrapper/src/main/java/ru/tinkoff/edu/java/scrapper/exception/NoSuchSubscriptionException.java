package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException(String message) {
        super(message);
    }

    public NoSuchSubscriptionException(Long chatId, URI url) {
        super("Subscription to link %s for chat id %d is not exists".formatted(url.toString(), chatId));
    }

}
