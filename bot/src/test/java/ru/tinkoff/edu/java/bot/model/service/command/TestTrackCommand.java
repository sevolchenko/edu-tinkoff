package ru.tinkoff.edu.java.bot.model.service.command;

import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.component.textprovider.TrackCommandTextProvider;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.TrackCommand;

public class TestTrackCommand extends TrackCommand {

    public TestTrackCommand() {
        super(new TestScrapperClient(), new TrackCommandTextProvider(Mockito.spy(MessageTemplates.class)));
    }

}
