package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.service.IBot;
import ru.tinkoff.edu.java.bot.service.IUserMessageProcessor;
import ru.tinkoff.edu.java.bot.service.command.*;
import ru.tinkoff.edu.java.bot.service.impl.Bot;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BotConfiguration {

    @Bean
    @Autowired
    public TelegramBot telegramBot(String botToken){
        return new TelegramBot(botToken);
    }

    @Bean
    @Autowired
    public List<Command> commands(StartCommand startCommand, HelpCommand helpCommand,
                                  TrackCommand trackCommand, UntrackCommand untrackCommand,
                                  ListCommand listCommand) {
        return new ArrayList<>(List.of(
                startCommand,
                helpCommand,
                trackCommand,
                untrackCommand,
                listCommand
        ));
    }

    @Bean
    @Autowired
    public IBot bot(TelegramBot telegramBot, IUserMessageProcessor userMessageProcessor) {
        var bot = new Bot(telegramBot, userMessageProcessor);

        bot.execute(new SetMyCommands(userMessageProcessor.commands().stream()
                .map(Command::toApiCommand)
                .toArray(BotCommand[]::new)
                ));

        bot.start();
        return bot;
    }

}
