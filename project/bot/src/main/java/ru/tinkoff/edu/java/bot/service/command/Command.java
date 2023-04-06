package ru.tinkoff.edu.java.bot.service.command;

import com.pengrad.telegrambot.model.BotCommand;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public sealed interface Command permits HelpCommand, StartCommand, TrackCommand, UntrackCommand, ListCommand {
    String command();

    String description();

    SendMessage handle(Update update);

    default boolean supports(Update update) {
        String text = update.message().text();
        return command().equals(text.split(" ")[0]);
    }

    default BotCommand toApiCommand() {
        return new BotCommand(command(), description());
    }
}
