package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JpaLinkService implements ILinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaTgChatRepository tgChatRepository;

    private final LinkMapper linkMapper;

    @Override
    public LinkOutput add(Long tgChatId, URI url) {
        return null;
    }

    @Override
    public LinkOutput remove(Long tgChatId, URI url) {
        return null;
    }

    @Override
    public Collection<LinkOutput> listAllForChat(Long tgChatId) {
        return null;
    }
}
