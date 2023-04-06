package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;

@RequiredArgsConstructor
@Component
public final class ListCommand implements Command {

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
            return new SendMessage(update.message().chat().id(), "Ты еще не отслеживаешь ни одной ссылки");
        }

        StringBuffer sb = new StringBuffer(String.format("Вот список ссылок, которые ты отслеживаешь (всего %d):",
                linkResponses.size()));

        linkResponses.links().forEach(linkResponse -> {
            sb.append("\n- ");
            sb.append(linkResponse.link().toString());
        });
        return new SendMessage(update.message().chat().id(), sb.toString())
                .disableWebPagePreview(true);
    }
}
