package ru.tinkoff.edu.java.bot.service.bot.impl;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.scrapper.exception.ScrapperClientException;
import ru.tinkoff.edu.java.bot.component.textprovider.ErrorTextProvider;
import ru.tinkoff.edu.java.bot.service.bot.IUserMessageProcessor;
import ru.tinkoff.edu.java.bot.service.command.Command;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserMessageProcessor implements IUserMessageProcessor {

    private final List<? extends Command> commands;
    private final ErrorTextProvider errorTextProvider;

    @Override
    public List<? extends Command> commands() {
        return commands;
    }

    @Override
    public SendMessage process(Update update) {

        try {
            var supportsCommands = commands().stream()
                    .filter(command -> command.supports(update))
                    .toList();

            if (supportsCommands.size() == 0) {
                log.warn("Processing update {} failed: unknown message: {}",
                    update.updateId(), update.message().text());

                var text = errorTextProvider.getUnknownMessageText();
                return new SendMessage(update.message().chat().id(), text);
            }

            if (supportsCommands.size() > 1) {
                StringBuffer sb = new StringBuffer();
                supportsCommands.forEach(command -> {
                    sb.append("[");
                    sb.append(command.getClass().getName());
                    sb.append("]");
                });
                log.error("Processing update {} failed: message {} can be handled by several commands: {}",
                        update.updateId(), update.message().text(), sb);

                var text = errorTextProvider.getUnknownCommandText(update.message().text());
                throw new IllegalArgumentException(text);
            }

            return supportsCommands.get(0).handle(update);

        } catch (ScrapperClientException e) {
            log.warn("Client exception {} thrown: {}",
                e.getApiErrorResponse().exceptionName(), e.getApiErrorResponse().exceptionMessage());

            return new SendMessage(update.message().chat().id(), errorTextProvider.getErrorMessage(e));

        } catch (Exception e) {
            log.warn("Exception {} thrown: {}", e.getClass().getName(), e.getMessage());

            return new SendMessage(update.message().chat().id(), errorTextProvider.getErrorMessage(e));
        }

    }
}
