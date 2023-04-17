package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.service.text.TextProvider.HelpTextProvider;

import java.util.List;

@Component
public class HelpCommand implements Command {

    private List<BotCommand> botCommands;

    @Lazy
    @Autowired
    public void setBotCommands(List<BotCommand> botCommands) {
        this.botCommands = botCommands;
    }

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) {
        var text = HelpTextProvider.buildHelpText(botCommands);
        return new SendMessage(update.message().chat().id(), text);
    }
}
