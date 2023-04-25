package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements ITgChatService {

    private final JdbcTgChatRepository tgChatRepository;

    @Override
    public TgChatOutput register(RegisterTgChatInput request) {
        Long tgChatId = tgChatRepository.add(request);
        return tgChatRepository.findByTgChatId(tgChatId);
    }

    @Override
    public TgChatOutput unregister(Long tgChatId) {
        return tgChatRepository.remove(tgChatId);
    }
}
