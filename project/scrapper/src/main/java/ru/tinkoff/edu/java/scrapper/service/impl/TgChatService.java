package ru.tinkoff.edu.java.scrapper.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

import java.time.OffsetDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TgChatService implements ITgChatService {

    private final ITgChatRepository tgChatRepository;

    @Override
    public TgChatOutput register(Long tgChatId, String username) {
        Long savedTgChatId = tgChatRepository.save(
                new AddTgChatInput(tgChatId, username, OffsetDateTime.now())
        );

        if (savedTgChatId == null) {
            throw new AlreadyRegisteredChatException(tgChatId);
        }

        return tgChatRepository.findById(tgChatId);
    }

    @Override
    public TgChatOutput unregister(Long tgChatId) {

        var res = tgChatRepository.remove(tgChatId);

        if (res == null) {
            throw new NoSuchChatException(tgChatId);
        }

        return res;
    }
}
