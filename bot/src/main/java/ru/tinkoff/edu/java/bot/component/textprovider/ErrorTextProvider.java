package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.scrapper.exception.ScrapperClientException;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;

@Component
@RequiredArgsConstructor
public class ErrorTextProvider {

    private final MessageTemplates messageTemplates;

    public String getUnknownMessageText() {
        return messageTemplates.errors().unknownMessageTemplate();
    }

    public String getUnknownCommandText(String command) {
        return messageTemplates.errors().unknownCommandTemplate()
                .formatted(command);
    }

    public String getErrorMessage(ScrapperClientException ex) {
        return messageTemplates.errors().clientErrorTemplate()
                .formatted(ex.getApiErrorResponse().description());
    }

    public String getErrorMessage(Exception ex) {
        return messageTemplates.errors().errorTemplate();
    }
}
