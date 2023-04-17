package ru.tinkoff.edu.java.bot.model.service.command;

import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.StartCommand;

public class TestStartCommand extends StartCommand {

    public TestStartCommand() {
        super(new TestScrapperClient());
    }

}
