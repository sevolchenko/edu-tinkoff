package ru.tinkoff.edu.java.bot.model.telegram;

import com.pengrad.telegrambot.model.Chat;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestChat extends Chat {

    private final Long id;

    @Override
    public Long id() {
        return id;
    }

}
