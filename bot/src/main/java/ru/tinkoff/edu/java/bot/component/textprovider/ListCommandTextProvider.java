package ru.tinkoff.edu.java.bot.component.textprovider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListCommandTextProvider {

    private final MessageTemplates messageTemplates;

    public String getEmptyLinksText() {
        return messageTemplates.commands().emptyListCommandTemplate();
    }

    public String getLinksListText(List<String> links) {
        StringBuilder sb = new StringBuilder();

        links.forEach(link -> {
            sb.append("\n- ");
            sb.append(link);
        });

        return messageTemplates.commands().listCommandTemplate()
                .formatted(sb);
    }

}
