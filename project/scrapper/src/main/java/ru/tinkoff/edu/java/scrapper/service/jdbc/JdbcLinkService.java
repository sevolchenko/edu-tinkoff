package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.scrapper.exception.InvalidLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NotSupportedLinkException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements ILinkService {

    private final LinkParser linkParser;
    private final JdbcLinkRepository linkRepository;

    @Override
    public LinkOutput add(Long tgChatId, URI url) {
        if (!linkParser.supports(url)) {
            throw new NotSupportedLinkException(String.format("Domain %s is not supported yet", url.getHost()));
        }
        if (linkParser.parse(url) == null) {
            throw new InvalidLinkException(String.format("Invalid link format: %s", url));
        }
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
