package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class JpaTgChatService implements ITgChatService {

    private final JpaTgChatRepository tgChatRepository;
    private final TgChatMapper tgChatMapper;

    @Override
    public TgChatOutput register(RegisterTgChatInput request) {
        TgChat tgChat = new TgChat()
                .setTgChatId(request.tgChatId())
                .setUsername(request.username())
                .setRegisteredAt(OffsetDateTime.now().toInstant());

        tgChatRepository.save(tgChat);
        return tgChatMapper.toOutput(tgChat);
    }

    @Override
    public TgChatOutput unregister(Long tgChatId) {
        return null;
    }

}
