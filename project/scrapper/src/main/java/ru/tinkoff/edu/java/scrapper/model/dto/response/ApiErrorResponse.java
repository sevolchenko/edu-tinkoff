package ru.tinkoff.edu.java.scrapper.model.dto.response;

public record ApiErrorResponse(String description, String code, String exceptionName,
                               String exceptionMessage, String[] stacktrace) {}