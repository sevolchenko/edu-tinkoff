package ru.tinkoff.edu.java.bot.service.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.exception.HttpClientException;
import ru.tinkoff.edu.java.bot.service.IUserMessageProcessor;
import ru.tinkoff.edu.java.bot.service.command.Command;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMessageProcessor implements IUserMessageProcessor {

    private final List<Command> commands;

//    public UserMessageProcessor(IScrapperClient scrapperClient) {
//        this.scrapperClient = scrapperClient;
//        commands = List.of(
//                new StartCommand(this.scrapperClient),
//                new HelpCommand(),
//                new TrackCommand(this.scrapperClient),
//                new UntrackCommand(this.scrapperClient),
//                new ListCommand(this.scrapperClient)
//        );
//    }

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {

        try{
            var supportsCommands = commands().stream()
                    .filter(command -> command.supports(update))
                    .toList();

            if (supportsCommands.size() == 0) {
                log.warn(String.format("Processing update %d failed: unknown message: %s", update.updateId(), update.message().text()));

                return new SendMessage(update.message().chat().id(),
                        "Извини, бот умеет общаться только через известные ему команды\n" +
                                "Для просмотра введи /help");
            }

            if (supportsCommands.size() > 1) {
                throw new IllegalArgumentException(String.format("Неизвестная команда: %s", update.message().text()));
            }

            return supportsCommands.get(0).handle(update);

        } catch (HttpClientException e) {
            log.warn(String.format("Exception %s thrown: %s", e.getClass().getName(), e.getApiErrorResponse().description()));

            return new SendMessage(update.message().chat().id(),
                    String.format("Произошла ошибка: %s", e.getApiErrorResponse().description()));
        } catch (Exception e) {
            log.warn(String.format("Exception %s thrown: %s", e.getClass().getName(), e.getMessage()));

            return new SendMessage(update.message().chat().id(), "Произошла ошибка, попробуйте еще раз...");
        }

    }
}
