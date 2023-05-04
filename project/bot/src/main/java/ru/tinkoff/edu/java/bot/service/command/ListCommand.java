package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.util.TextProvider.ListTextProvider;

@RequiredArgsConstructor
@Component
public class ListCommand implements Command {

    private final IScrapperClient scrapperClient;

    @Override
    public String command() {
        return "/list";
    }

    @Override
    public String description() {
        return "показать список отслеживаемых ссылок";
    }

    @Override
    public SendMessage handle(Update update) {
        var linkResponses = scrapperClient.getLinks(update.message().chat().id());

        if (linkResponses.size() == null || linkResponses.size() == 0) {
            var text = ListTextProvider.buildEmptyLinksText();
            return new SendMessage(update.message().chat().id(), text);
        }

        var text = ListTextProvider.buildLinksListText(linkResponses.links().stream()
                .map(linkResponse -> linkResponse.link().toString())
                .toList());
        return new SendMessage(update.message().chat().id(), text)
                .disableWebPagePreview(true);
    }
}
