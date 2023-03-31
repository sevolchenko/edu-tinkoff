package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.scrapper.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyAddedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;

import java.util.Arrays;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScrapperExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return buildApiErrorResponse(ex, "Bad request", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildApiErrorResponse(ex, "Bad arguments", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchChatException.class)
    protected ResponseEntity<Object> handleNoSuchChatException(NoSuchChatException ex) {
        return buildApiErrorResponse(ex, "No chat found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyRegisteredChatException.class)
    protected ResponseEntity<Object> handleAlreadyRegisteredChatException(AlreadyRegisteredChatException ex) {
        return buildApiErrorResponse(ex, "Chat already exists", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchLinkException.class)
    protected ResponseEntity<Object> handleNoSuchLinkException(NoSuchLinkException ex) {
        return buildApiErrorResponse(ex, "No link found", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyAddedLinkException.class)
    protected ResponseEntity<Object> handleAlreadyAddedLinkException(AlreadyAddedLinkException ex) {
        return buildApiErrorResponse(ex, "Link already added", HttpStatus.CONFLICT);
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