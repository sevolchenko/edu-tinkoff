package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class NoSuchSubscriptionException extends RuntimeException {

    public NoSuchSubscriptionException(String message) {
        super(message);
    }

    public NoSuchSubscriptionException(Long chatId, URI url) {
        super(String.format("Subscription to link %s for chat id %d is not exists", url.toString(), chatId));
    }

}
