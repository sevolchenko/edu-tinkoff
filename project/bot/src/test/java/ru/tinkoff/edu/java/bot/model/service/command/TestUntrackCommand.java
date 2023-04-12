package ru.tinkoff.edu.java.bot.model.service.command;

import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.UntrackCommand;

public class TestUntrackCommand extends UntrackCommand {

    public TestUntrackCommand() {
        super(new TestScrapperClient());
    }

}
