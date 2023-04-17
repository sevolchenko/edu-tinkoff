package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.text.TextProvider.TrackTextProvider;
import ru.tinkoff.edu.java.bot.util.UrlUtils;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class TrackCommand implements Command {

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
                var text = TrackTextProvider.buildNoLinkErrorText();
                return new SendMessage(update.message().chat().id(), text);
            }

            URI link = UrlUtils.create(message.substring(spacePos + 1));

            LinkResponse linkResponse = scrapperClient.addLink(update.message().chat().id(),
                    new AddLinkRequest(link));

            var text = TrackTextProvider.buildSuccessfullyAddedLinkText(linkResponse.link().toString());
            return new SendMessage(update.message().chat().id(), text);
        } catch (IllegalArgumentException e) {
            var text = TrackTextProvider.buildInvalidLinkText();
            return new SendMessage(update.message().chat().id(), text)
                    .disableWebPagePreview(true);
        }
    }
}
