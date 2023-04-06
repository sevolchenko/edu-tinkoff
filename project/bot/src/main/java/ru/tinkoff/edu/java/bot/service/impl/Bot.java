package ru.tinkoff.edu.java.bot.service.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.bot.service.IBot;
import ru.tinkoff.edu.java.bot.service.IUserMessageProcessor;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class Bot implements IBot {

    private final TelegramBot telegramBot;
    private final IUserMessageProcessor userMessageProcessor;

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {
        log.info(String.format("Bot executed request %s", request.getParameters().toString()));

        telegramBot.execute(request);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info(String.format("Bot received update %d from chat %d, message: %s",
                    update.updateId(), update.message().chat().id(), update.message().text()));

            execute(userMessageProcessor.process(update));
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    @Override
    public void start() {
        telegramBot.setUpdatesListener(this);

        log.info("Bot started");
    }

    @Override
    public void close() {
        telegramBot.shutdown();

        log.info("Bot closed");
    }
}
