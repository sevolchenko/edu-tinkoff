package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.TgChatResponse;

public interface ITgChatService {

    TgChatResponse register(RegisterTgChatRequest request);
    TgChatResponse unregister(Long tgChatId);

}
