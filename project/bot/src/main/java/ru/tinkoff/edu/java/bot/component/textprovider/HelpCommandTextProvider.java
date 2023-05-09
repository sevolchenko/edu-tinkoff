package ru.tinkoff.edu.java.bot.component.textprovider;

import com.pengrad.telegrambot.model.BotCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpCommandTextProvider {

    private final MessageTemplates messageTemplates;

    public String getHelpText(List<BotCommand> botCommands) {
        StringBuilder sb = new StringBuilder();

        botCommands.forEach(command -> {
            sb.append("\n");
            sb.append(command.command());
            sb.append(" - ");
            sb.append(command.description());
        });

        return messageTemplates.commands().helpCommandTemplate()
                .formatted(sb);
    }

}
