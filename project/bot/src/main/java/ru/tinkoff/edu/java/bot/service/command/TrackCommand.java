package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.util.UrlUtils;

import java.net.URI;

@RequiredArgsConstructor
@Component
public final class TrackCommand implements Command {

    private final IScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/track";
    }

    @Override
    public String description() {
        return "начать отслеживание ссылки";
    }

    @Override
    public SendMessage handle(Update update) {
        try {
            String message = update.message().text();

            int spacePos = message.indexOf(' ');

            if (spacePos == -1) {
                return new SendMessage(update.message().chat().id(),
                        "После команды через пробел нужно указать ссылку, которую нужно отследить");
            }

            URI link = UrlUtils.create(message.substring(spacePos + 1));

            LinkResponse linkResponse = scrapperClient.addLink(update.message().chat().id(),
                    new AddLinkRequest(link));

            return new SendMessage(update.message().chat().id(),
                    String.format("Ссылка %s успешно добавлена к отслеживаемым!\n" +
                            "Мы пришлем уведомление, когда по адресу произойдут изменения :)", linkResponse.link()));
        } catch (IllegalArgumentException e) {
            return new SendMessage(update.message().chat().id(), "Ссылка указана неверно")
                    .disableWebPagePreview(true);
        }
    }
}
