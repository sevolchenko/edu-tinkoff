package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class AlreadySubscribedLinkException extends RuntimeException {

    public AlreadySubscribedLinkException(String message) {
        super(message);
    }

    public AlreadySubscribedLinkException(Long chatId, URI url) {
        super("Subscription to link %s for chat id %d already added".formatted(url.toString(), chatId));
    }

}
