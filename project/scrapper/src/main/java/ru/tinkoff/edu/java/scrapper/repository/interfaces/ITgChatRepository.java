package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;

import java.util.Collection;

public interface ITgChatRepository {

    Long add(RegisterTgChatInput request);

    TgChatOutput remove(Long tgChatId);

    Collection<TgChatOutput> findAll();

    TgChatOutput findByTgChatId(Long tgChatId);

}
