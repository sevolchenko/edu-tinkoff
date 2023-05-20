package ru.tinkoff.edu.java.scrapper.exception;

public class NoSuchChatException extends RuntimeException {

    public NoSuchChatException(String message) {
        super(message);
    }

    public NoSuchChatException(Long chatId) {
        super("There is no chat with id %d".formatted(chatId));
    }

}
