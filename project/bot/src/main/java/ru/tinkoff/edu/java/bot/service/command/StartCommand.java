package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.service.text.TextProvider.StartTextProvider;

@RequiredArgsConstructor
@Component
public class StartCommand implements Command {

    private final IScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/start";
    }

    @Override
    public String description() {
        return "зарегистрировать пользователя";
    }

    @Override
    public SendMessage handle(Update update) {
        scrapperClient.registerChatById(update.message().chat().id(), update.message().chat().username());
        var text = StartTextProvider.buildStartMessage();
        return new SendMessage(update.message().chat().id(), text)
                .disableWebPagePreview(true);
    }
}
