package ru.tinkoff.edu.java.bot.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.bot.model.dto.response.ApiErrorResponse;

import java.util.Arrays;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BotExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return buildApiErrorResponse(ex, "Bad request", HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Object> buildApiErrorResponse(Exception ex, String description, HttpStatus status) {
        String[] stackTrace = Arrays.stream(ex.getStackTrace())
                .map(StackTraceElement::toString)
                .toArray(String[]::new);
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(description,
                String.valueOf(status.value()), ex.getClass().getName(), ex.getMessage(), stackTrace);
        return new ResponseEntity<>(apiErrorResponse, status);
    }

}
