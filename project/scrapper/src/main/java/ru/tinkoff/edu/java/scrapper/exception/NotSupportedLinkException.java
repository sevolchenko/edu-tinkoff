package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class NotSupportedLinkException extends RuntimeException {

    public NotSupportedLinkException(String message) {
        super(message);
    }

    public NotSupportedLinkException(URI url) {
        super(String.format("Domain %s is not supported yet", url.getHost()));
    }

}
