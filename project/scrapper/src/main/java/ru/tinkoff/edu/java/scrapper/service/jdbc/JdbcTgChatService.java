package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.TgChatResponse;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements ITgChatService {

    private final ITgChatRepository tgChatRepository;

    @Override
    public TgChatResponse register(RegisterTgChatRequest request) {
        Long tgChatId = tgChatRepository.add(request);
        return tgChatRepository.findByTgChatId(tgChatId);
    }

    @Override
    public TgChatResponse unregister(Long tgChatId) {
        return tgChatRepository.remove(tgChatId);
    }
}
