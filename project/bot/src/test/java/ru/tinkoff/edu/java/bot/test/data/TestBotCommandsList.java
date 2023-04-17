package ru.tinkoff.edu.java.bot.test.data;

import com.pengrad.telegrambot.model.BotCommand;
import ru.tinkoff.edu.java.bot.model.service.command.*;
import ru.tinkoff.edu.java.bot.service.command.HelpCommand;

import java.util.List;

public class TestBotCommandsList {

    public static final List<BotCommand> BOT_COMMANDS = List.of(
            new TestStartCommand().toApiCommand(),
            new HelpCommand().toApiCommand(),
            new TestListCommand().toApiCommand(),
            new TestTrackCommand().toApiCommand(),
            new TestUntrackCommand().toApiCommand()
    );

}
