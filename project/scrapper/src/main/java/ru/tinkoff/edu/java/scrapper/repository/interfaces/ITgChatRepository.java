package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.TgChatResponse;

import java.util.Collection;

@Repository
public interface ITgChatRepository {

    Long add(RegisterTgChatRequest request);

    TgChatResponse remove(Long tgChatId);

    Collection<TgChatResponse> findAll();

}
