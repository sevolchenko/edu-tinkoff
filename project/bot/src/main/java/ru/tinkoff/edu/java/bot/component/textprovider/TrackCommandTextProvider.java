package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;

@Component
@RequiredArgsConstructor
public class TrackCommandTextProvider {

    private final MessageTemplates messageTemplates;

    public String getNoLinkErrorText() {
        return messageTemplates.errors().noLinkTemplate();
    }

    public String getSuccessfullyAddedLinkText(String link) {
        return messageTemplates.commands().successTrackTemplate()
                .formatted(link);
    }

    public String getInvalidLinkText() {
        return messageTemplates.errors().invalidLinkTemplate();
    }

}
