package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;

@Component
@RequiredArgsConstructor
public class UntrackCommandTextProvider {

    private final MessageTemplates messageTemplates;

    public String getNoLinkErrorText() {
        return messageTemplates.errors().noLinkTemplate();
    }

    public String getSuccessfullyRemovedLinkText(String link) {
        return messageTemplates.commands().successUntrackTemplate()
                .formatted(link);
    }

    public String getInvalidLinkText() {
        return messageTemplates.errors().invalidLinkTemplate();
    }

}
