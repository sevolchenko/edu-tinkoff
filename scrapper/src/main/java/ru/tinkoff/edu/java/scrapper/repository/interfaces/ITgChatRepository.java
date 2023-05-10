package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;

import java.util.List;

public interface ITgChatRepository {

    Long save(AddTgChatInput tgChat);

    TgChatOutput remove(Long tgChatId);

    List<TgChatOutput> findAll();

    TgChatOutput findById(Long tgChatId);

}
