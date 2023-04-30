package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;

public interface ITgChatService {

    TgChatOutput register(Long tgChatId, String username);
    TgChatOutput unregister(Long tgChatId);

}
