package ru.tinkoff.edu.java.bot.service.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import java.util.List;

import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.service.command.Command;

@Component
public interface IUserMessageProcessor {
    List<? extends Command> commands();

    SendMessage process(Update update);
}
