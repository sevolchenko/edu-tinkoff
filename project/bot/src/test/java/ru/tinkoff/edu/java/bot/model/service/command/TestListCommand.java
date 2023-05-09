package ru.tinkoff.edu.java.bot.model.service.command;

import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.component.textprovider.ListCommandTextProvider;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.ListCommand;

public class TestListCommand extends ListCommand {

    public TestListCommand() {
        super(new TestScrapperClient(), new ListCommandTextProvider(Mockito.spy(MessageTemplates.class)));
    }

}
