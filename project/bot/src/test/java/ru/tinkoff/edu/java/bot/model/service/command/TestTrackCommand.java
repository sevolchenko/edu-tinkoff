package ru.tinkoff.edu.java.bot.model.service.command;

import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.TrackCommand;

public class TestTrackCommand extends TrackCommand {

    public TestTrackCommand() {
        super(new TestScrapperClient());
    }

}
