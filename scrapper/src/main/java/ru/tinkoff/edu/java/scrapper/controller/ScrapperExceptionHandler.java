package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.tinkoff.edu.java.scrapper.exception.*;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ApiErrorResponse;

import java.util.Arrays;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ScrapperExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        return buildApiErrorResponse(ex, "Неправильный запрос", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return buildApiErrorResponse(ex, "Неправильные параметры запроса", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchChatException.class)
    protected ResponseEntity<Object> handleNoSuchChatException(NoSuchChatException ex) {
        return buildApiErrorResponse(ex, "Чата еще нет в базе, зарегистрируйтесь", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyRegisteredChatException.class)
    protected ResponseEntity<Object> handleAlreadyRegisteredChatException(AlreadyRegisteredChatException ex) {
        return buildApiErrorResponse(ex, "Чат уже существует", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchLinkException.class)
    protected ResponseEntity<Object> handleNoSuchLinkException(NoSuchLinkException ex) {
        return buildApiErrorResponse(ex, "Ссылка не найдена", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadySubscribedLinkException.class)
    protected ResponseEntity<Object> handleAlreadyAddedLinkException(AlreadySubscribedLinkException ex) {
        return buildApiErrorResponse(ex, "Подписка уже добавлена", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoSuchSubscriptionException.class)
    protected ResponseEntity<Object> handleNoSuchSubscriptionException(NoSuchSubscriptionException ex) {
        return buildApiErrorResponse(ex, "Такой подписки не существует", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotSupportedLinkException.class)
    protected ResponseEntity<Object> handleNotSupportedLinkException(NotSupportedLinkException ex) {
        return buildApiErrorResponse(ex, "Ссылки с этого домена не поддерживаются", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidLinkException.class)
    protected ResponseEntity<Object> handleNotSupportedLinkException(InvalidLinkException ex) {
        return buildApiErrorResponse(ex, "Неправильный формат ссылки, сопоставьте с примером", HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NotFoundLinkException.class)
    protected ResponseEntity<Object> handleNotFoundLinkException(NotFoundLinkException ex) {
        return buildApiErrorResponse(ex, "Ссылка не найдена или к ней нет доступа", HttpStatus.CONFLICT);
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