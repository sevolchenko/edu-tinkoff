package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public final class HelpCommand implements Command {

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
        StringBuffer sb = new StringBuffer("Вот список доступных команд:");

        botCommands.forEach(command -> {
            sb.append("\n");
            sb.append(command.command());
            sb.append(" - ");
            sb.append(command.description());
        });
        return new SendMessage(update.message().chat().id(), sb.toString());
    }
}