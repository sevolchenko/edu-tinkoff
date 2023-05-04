package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.request.SetMyCommands;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.bot.service.bot.IBot;
import ru.tinkoff.edu.java.bot.service.bot.IUserMessageProcessor;
import ru.tinkoff.edu.java.bot.service.command.Command;
import ru.tinkoff.edu.java.bot.service.bot.impl.Bot;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class BotConfiguration {

    @Bean
    public TelegramBot telegramBot(String botToken){
        return new TelegramBot(botToken);
    }

    @Bean
    public List<BotCommand> botCommands(List<? extends Command> commands) {
        var list = commands.stream()
                .map(Command::toApiCommand)
                .collect(Collectors.toList());
        Collections.reverse(list);
        return list;
    }

    @Bean
    public IBot bot(TelegramBot telegramBot, IUserMessageProcessor userMessageProcessor, List<BotCommand> botCommands) {
        var bot = new Bot(telegramBot, userMessageProcessor);

        bot.execute(new SetMyCommands(botCommands.toArray(BotCommand[]::new)));

        bot.start();
        return bot;
    }

}
