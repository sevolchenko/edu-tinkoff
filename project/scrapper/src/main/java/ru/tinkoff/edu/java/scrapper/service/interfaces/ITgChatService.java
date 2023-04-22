package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;

public interface ITgChatService {

    TgChatOutput register(RegisterTgChatInput request);
    TgChatOutput unregister(Long tgChatId);

}
