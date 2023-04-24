package ru.tinkoff.edu.java.bot.util;

import java.net.URI;
import org.apache.commons.validator.routines.UrlValidator;

public class UrlUtils {

    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    private UrlUtils() {}

    public static URI create(String link) {

        if (!URL_VALIDATOR.isValid(link)) {
            throw new IllegalArgumentException("Invalid link");
        }

        return URI.create(link);

    }

}
