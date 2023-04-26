package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.util.Collection;

@RequiredArgsConstructor
public class JdbcLinkService implements ILinkService {

    private final JdbcLinkRepository linkRepository;

    @Override
    public LinkOutput add(Long tgChatId, URI url) {
        var request = new SubscriptionInput(tgChatId, url.toString());
        linkRepository.add(request);
        return linkRepository.findById(request);
    }

    @Override
    public LinkOutput remove(Long tgChatId, URI url) {
        return linkRepository.remove(new SubscriptionInput(tgChatId, url.toString()));
    }

    @Override
    public Collection<LinkOutput> listAllForChat(Long tgChatId) {
        return linkRepository.findAll(tgChatId);
    }

}
