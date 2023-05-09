package ru.tinkoff.edu.java.bot.model.dto.response;

public record ApiErrorResponse(String description, String code, String exceptionName,
                               String exceptionMessage, String[] stacktrace) {
}

