package ru.tinkoff.edu.java.scrapper.exception;

public class AlreadyRegisteredChatException extends RuntimeException {

    public AlreadyRegisteredChatException(String message) {
        super(message);
    }

    public AlreadyRegisteredChatException(Long chatId) {
        super("Chat already registered: %d".formatted(chatId));
    }

}
