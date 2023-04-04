package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public final class HelpCommand implements Command {

    // todo: Неправильное автосвязывание, для commands нужен бин HelpCommand, а для HelpCommand - commands и он приходит неполный
    private final List<Command> commands;

    @Override
    public String command() {
        return "/help";
    }

    @Override
    public String description() {
        return "вывести окно с командами";
    }

    @Override
    public SendMessage handle(Update update) { // todo: Может быть, использовать хардкод вместо commands
        StringBuffer sb = new StringBuffer("Вот список доступных команд:");

        commands.forEach(command -> { // todo: Правильный порядок
            sb.append("\n");
            sb.append(command.command());
            sb.append(" - ");
            sb.append(command.description());
        });
        return new SendMessage(update.message().chat().id(), sb.toString());
//        return new SendMessage(update.message().chat().id(),
//        """
//                        Вот список доступных команд:\s
//                        /help - вывести окно с командами
//                        /track - начать отслеживание ссылки
//                        /untrack - прекратить отслеживание ссылки
//                        /list - показать список отслеживаемых ссылок"""
//        );
    }
}
