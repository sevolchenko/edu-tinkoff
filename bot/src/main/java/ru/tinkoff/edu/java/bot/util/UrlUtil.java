package ru.tinkoff.edu.java.bot.util;

import org.apache.commons.validator.routines.UrlValidator;

import java.net.URI;

public final class UrlUtil {

    private static final UrlValidator URL_VALIDATOR;

    static {
        URL_VALIDATOR = new UrlValidator();
    }

    private UrlUtil() {}

    public static URI create(String link) {

        if (!URL_VALIDATOR.isValid(link)) {
            throw new IllegalArgumentException("Invalid link");
        }

        return URI.create(link);

    }

}
