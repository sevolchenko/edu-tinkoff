package ru.tinkoff.edu.java.bot.model.service;

import org.mapstruct.factory.Mappers;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.configuration.properties.mapping.PropertiesMapper;

public class TestMessageTemplates {

    private static final MessageTemplates TEMPLATES;

    static {
        TEMPLATES = Mappers.getMapper(PropertiesMapper.class).fillDefaults(new MessageTemplates());
    }

    public static MessageTemplates get() {
        return TEMPLATES;
    }

}
