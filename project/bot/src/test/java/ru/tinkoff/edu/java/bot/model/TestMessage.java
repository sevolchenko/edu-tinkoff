package ru.tinkoff.edu.java.bot.model;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class TestMessage extends Message {

    private final String text;
    private Chat chat;

    @Override
    public String text() {
        return text;
    }

    @Override
    public Chat chat() {
        return chat;
    }

}
