package ru.tinkoff.edu.java.scrapper.exception;

public class AlreadyRegisteredChatException extends RuntimeException {

    public AlreadyRegisteredChatException(String message) {
        super(message);
    }

    public AlreadyRegisteredChatException(Long chatId) {
        super(String.format("Chat already registered: %d", chatId));
    }

}
