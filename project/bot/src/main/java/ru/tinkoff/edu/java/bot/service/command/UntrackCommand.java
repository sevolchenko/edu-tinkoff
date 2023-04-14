package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.util.UrlUtils;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class UntrackCommand implements Command {

    private final IScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/untrack";
    }

    @Override
    public String description() {
        return "прекратить отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        try {
            String message = update.message().text();

            int spacePos = message.indexOf(' ');

            if (spacePos == -1) {
                return new SendMessage(update.message().chat().id(),
                        "После команды через пробел нужно указать ссылку, которую нужно удалить из отслеживаемых");
            }

            URI link = UrlUtils.create(message.substring(spacePos + 1));

            LinkResponse linkResponse = scrapperClient.deleteLink(update.message().chat().id(),
                    new RemoveLinkRequest(link));

            return new SendMessage(update.message().chat().id(),
                    String.format("Ссылка %s успешно удалена из отслеживаемых!\n" +
                            "Мы больше не пришлем уведомление :(", linkResponse.link()));
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.message().chat().id(), "Ссылка указана неверно")
                    .disableWebPagePreview(true);
        }
    }
}
