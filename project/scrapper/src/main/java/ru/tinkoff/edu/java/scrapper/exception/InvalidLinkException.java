package ru.tinkoff.edu.java.scrapper.exception;

import java.net.URI;

public class InvalidLinkException extends IllegalArgumentException {

    public InvalidLinkException(String message) {
        super(message);
    }

    public InvalidLinkException(URI url) {
        super(String.format("Invalid link format: %s", url));
    }

}
