package ru.tinkoff.edu.java.bot.model.telegram;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUpdate extends Update {

    private final Message message;

    @Override
    public Message message() {
        return message;
    }
}
