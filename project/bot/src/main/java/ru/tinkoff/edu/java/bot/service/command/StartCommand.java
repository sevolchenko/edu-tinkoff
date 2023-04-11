package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;

@RequiredArgsConstructor
@Component
public final class StartCommand implements Command {

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
        scrapperClient.registerChatById(update.message().chat().id());
        return new SendMessage(update.message().chat().id(),
                """
                        Привет! Добро пожаловать!
                        Link Monitoring Bot - бот, который умеет отслеживать ссылки
                        Бот поддерживает два вида ссылок:
                        - GitHub (пример: https://github.com/sevolchenko/edu-tinkoff)
                        - StackOverflow (пример: https://stackoverflow.com/questions/10604298/spring-component-versus-bean)
                        Чтобы просмотреть команды бота, введи /help.""")
                .disableWebPagePreview(true);
    }
}