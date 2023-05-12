package ru.tinkoff.edu.java.bot.client.scrapper.exception;

import lombok.Getter;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ApiErrorResponse;

public class ScrapperClientException extends RuntimeException {

    @Getter
    private final ApiErrorResponse apiErrorResponse;

    public ScrapperClientException(ApiErrorResponse apiErrorResponse) {
        super(apiErrorResponse.exceptionMessage());
        this.apiErrorResponse = apiErrorResponse;
    }
}
