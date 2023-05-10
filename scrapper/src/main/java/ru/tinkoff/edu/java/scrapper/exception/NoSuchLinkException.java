package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class NoSuchLinkException extends RuntimeException {

    public NoSuchLinkException(String message) {
        super(message);
    }

    public NoSuchLinkException(URI url) {
        super(String.format("There is no link with url %s", url));
    }

}
