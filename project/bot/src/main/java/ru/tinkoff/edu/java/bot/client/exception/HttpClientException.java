package ru.tinkoff.edu.java.bot.client.exception;

import lombok.Getter;
import ru.tinkoff.edu.java.bot.client.dto.response.ApiErrorResponse;

@Getter
public class HttpClientException extends RuntimeException {

    private final ApiErrorResponse apiErrorResponse;

    public HttpClientException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
        this.apiErrorResponse = apiErrorResponse;
    }
}
