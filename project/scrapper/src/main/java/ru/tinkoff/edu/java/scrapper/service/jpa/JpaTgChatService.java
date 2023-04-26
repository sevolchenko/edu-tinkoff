package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

import java.time.OffsetDateTime;

@RequiredArgsConstructor
@Transactional
public class JpaTgChatService implements ITgChatService {

    private final JpaTgChatRepository tgChatRepository;
    private final TgChatMapper tgChatMapper;

    @Override
    public TgChatOutput register(RegisterTgChatInput request) {
        Long tgChatId = request.tgChatId();

        if (tgChatRepository.existsById(tgChatId)) {
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", tgChatId));
        }

        TgChat res = tgChatRepository.save(new TgChat()
                .setTgChatId(tgChatId)
                .setUsername(request.username())
                .setRegisteredAt(OffsetDateTime.now().toInstant()));


        return tgChatMapper.toOutput(res);
    }

    @Override
    public TgChatOutput unregister(Long tgChatId) {
        var tgChat = tgChatRepository.deleteTgChatByTgChatId(tgChatId)
                .orElseThrow(() -> new NoSuchChatException(String.format("There is no chat with id %d", tgChatId)));
        return tgChatMapper.toOutput(tgChat);
    }

}
