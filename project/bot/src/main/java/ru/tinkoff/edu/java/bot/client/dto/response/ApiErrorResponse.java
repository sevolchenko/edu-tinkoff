package ru.tinkoff.edu.java.bot.client.dto.response;

public record ApiErrorResponse(String description, String code, String exceptionName,
                               String exceptionMessage, String[] stacktrace) {}