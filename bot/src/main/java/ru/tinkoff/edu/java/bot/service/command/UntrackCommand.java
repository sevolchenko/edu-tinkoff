package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.component.textprovider.UntrackCommandTextProvider;
import ru.tinkoff.edu.java.bot.util.UrlUtil;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;

import java.net.URI;

@RequiredArgsConstructor
@Component
public class UntrackCommand implements Command {

    private final IScrapperClient scrapperClient;
    private final UntrackCommandTextProvider textProvider;

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
                var text = textProvider.getNoLinkErrorText();
                return new SendMessage(update.message().chat().id(), text);
            }

            URI link = UrlUtil.create(message.substring(spacePos + 1));

            LinkResponse linkResponse = scrapperClient.deleteLink(update.message().chat().id(),
                    new RemoveLinkRequest(link));

            var text = textProvider.getSuccessfullyRemovedLinkText(linkResponse.link().toString());
            return new SendMessage(update.message().chat().id(), text);
        } catch (IllegalArgumentException e) {
            var text = textProvider.getInvalidLinkText();
            return new SendMessage(update.message().chat().id(), text)
                    .disableWebPagePreview(true);
        }
    }
}
