package ru.tinkoff.edu.java.bot.service.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.service.command.Command;

import java.util.List;

@Component
public interface IUserMessageProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
