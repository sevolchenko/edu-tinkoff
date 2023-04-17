package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.service.text.TextProvider.UntrackTextProvider;
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
                var text = UntrackTextProvider.buildNoLinkErrorText();
                return new SendMessage(update.message().chat().id(), text);
            }

            URI link = UrlUtils.create(message.substring(spacePos + 1));

            LinkResponse linkResponse = scrapperClient.deleteLink(update.message().chat().id(),
                    new RemoveLinkRequest(link));

            var text = UntrackTextProvider.buildSuccessfullyRemovedLinkText(linkResponse.link().toString());
            return new SendMessage(update.message().chat().id(), text);
        } catch (IllegalArgumentException e) {
            var text = UntrackTextProvider.buildInvalidLinkText();
            return new SendMessage(update.message().chat().id(), text)
                    .disableWebPagePreview(true);
        }
    }
}
