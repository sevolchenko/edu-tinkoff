package ru.tinkoff.edu.java.bot.model.service.command;

import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.component.textprovider.HelpCommandTextProvider;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.service.command.HelpCommand;

public class TestHelpCommand extends HelpCommand {

    public TestHelpCommand() {
        super(new HelpCommandTextProvider(Mockito.spy(MessageTemplates.class)));
    }

}
