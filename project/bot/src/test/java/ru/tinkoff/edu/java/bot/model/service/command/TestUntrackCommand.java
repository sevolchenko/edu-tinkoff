package ru.tinkoff.edu.java.bot.model.service.command;

import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.component.textprovider.UntrackCommandTextProvider;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.UntrackCommand;

public class TestUntrackCommand extends UntrackCommand {

    public TestUntrackCommand() {
        super(new TestScrapperClient(), new UntrackCommandTextProvider(Mockito.spy(MessageTemplates.class)));
    }

}
