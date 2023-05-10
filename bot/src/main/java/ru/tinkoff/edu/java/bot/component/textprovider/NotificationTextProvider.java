package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkEvent;

@Component
@RequiredArgsConstructor
public class NotificationTextProvider {

    private final MessageTemplates messageTemplates;

    public String getNotificationText(LinkEvent event, String link) {
        switch (event) {
            case UPDATED, default -> {
                return messageTemplates.events().updateNotificationTemplate()
                        .formatted(link);
            }
            case BRANCHES_COUNT_INCREASED -> {
                return messageTemplates.events().branchesCountIncNotificationTemplate()
                        .formatted(link);
            }
            case BRANCHES_COUNT_DECREASED -> {
                return messageTemplates.events().branchesCountDecNotificationTemplate()
                        .formatted(link);
            }
            case ANSWERS_COUNT_INCREASED -> {
                return messageTemplates.events().answersCountIncNotificationTemplate()
                        .formatted(link);
            }
        }
    }
}
