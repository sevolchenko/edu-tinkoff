package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Transactional
public class JpaTgChatService implements ITgChatService {

    private final JpaTgChatRepository tgChatRepository;
    private final TgChatOutputMapper mapper;

    @Override
    public TgChatOutput register(Long tgChatId, String username) {
        if (tgChatRepository.existsById(tgChatId)) {
            throw new AlreadyRegisteredChatException(tgChatId);
        }

        TgChat res = tgChatRepository.save(new TgChat()
                .setTgChatId(tgChatId)
                .setUsername(username)
                .setRegisteredAt(OffsetDateTime.now().toInstant()));


        return mapper.map(res);
    }

    @Override
    public TgChatOutput unregister(Long tgChatId) {
        var deleted = tgChatRepository.removeTgChatByTgChatIdEquals(tgChatId);
        if (deleted.isEmpty()) {
            throw new NoSuchChatException(tgChatId);
        }
        return mapper.map(deleted.get(0));
    }

}
