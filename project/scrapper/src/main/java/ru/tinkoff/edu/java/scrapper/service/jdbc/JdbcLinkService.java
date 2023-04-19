package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.scrapper.exception.NotSupportedLinkException;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.SubscriptionRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements ILinkService {

    private final LinkParser linkParser;
    private final ILinkRepository linkRepository;

    @Override
    public LinkResponse add(Long tgChatId, URI url) {
        if (!linkParser.supports(url)) {
            throw new NotSupportedLinkException(String.format("Link %s is not supported yet", url));
        }
        var request = new SubscriptionRequest(tgChatId, url.toString());
        linkRepository.add(request);
        return linkRepository.findById(request);
    }

    @Override
    public LinkResponse remove(Long tgChatId, URI url) {
        return linkRepository.remove(new SubscriptionRequest(tgChatId, url.toString()));
    }

    @Override
    public Collection<LinkResponse> listAll(Long tgChatId) {
        return linkRepository.findAll(tgChatId);
    }

}
