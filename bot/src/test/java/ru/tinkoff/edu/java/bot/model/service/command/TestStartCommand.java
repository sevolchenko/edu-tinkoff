package ru.tinkoff.edu.java.bot.model.service.command;

import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.component.textprovider.StartCommandTextProvider;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.StartCommand;

public class TestStartCommand extends StartCommand {

    public TestStartCommand() {
        super(new TestScrapperClient(), new StartCommandTextProvider(Mockito.spy(MessageTemplates.class)));
    }

}
