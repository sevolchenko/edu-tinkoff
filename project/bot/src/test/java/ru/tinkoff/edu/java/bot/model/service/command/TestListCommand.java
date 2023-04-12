package ru.tinkoff.edu.java.bot.model.service.command;

import ru.tinkoff.edu.java.bot.model.service.TestScrapperClient;
import ru.tinkoff.edu.java.bot.service.command.ListCommand;

public class TestListCommand extends ListCommand {

    public TestListCommand() {
        super(new TestScrapperClient());
    }

}
