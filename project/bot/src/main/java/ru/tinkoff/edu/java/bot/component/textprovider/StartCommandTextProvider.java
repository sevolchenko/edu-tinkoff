package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;


@Component
@RequiredArgsConstructor
public class StartCommandTextProvider {

    private final MessageTemplates messageTemplates;

    public String getStartMessage() {
        return messageTemplates.commands().startCommandTemplate();
    }

}
