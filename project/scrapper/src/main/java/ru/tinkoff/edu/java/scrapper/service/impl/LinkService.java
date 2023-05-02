package ru.tinkoff.edu.java.scrapper.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.exception.AlreadySubscribedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchSubscriptionException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddSubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionIdInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ISubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;

@Transactional
@RequiredArgsConstructor
public class LinkService implements ILinkService {

    private final ILinkRepository linkRepository;
    private final ISubscriptionRepository subscriptionRepository;
    private final ITgChatRepository tgChatRepository;

    private final LinkProcessor linkProcessor;

    @Override
    public LinkOutput add(Long tgChatId, URI url) {

        if (tgChatRepository.findById(tgChatId) == null) {
            throw new NoSuchChatException(tgChatId);
        }

        LinkOutput output = linkRepository.findByUrl(url.toString());

        if (output == null) {
            var state = linkProcessor.getState(url);

            Long linkId = linkRepository.save(
                    new AddLinkInput(url.toString(), state, OffsetDateTime.now(), OffsetDateTime.now())
            );

            output = linkRepository.findById(linkId);
        }

        if (subscriptionRepository.save(new AddSubscriptionInput(
                tgChatId, output.getLinkId(), OffsetDateTime.now())) == null) {
            throw new AlreadySubscribedLinkException(tgChatId, url);
        }

        return output;
    }

    @Override
    public LinkOutput remove(Long tgChatId, URI url) {

        if (tgChatRepository.findById(tgChatId) == null) {
            throw new NoSuchChatException(tgChatId);
        }

        var linkOutput = linkRepository.findByUrl(url.toString());

        if (linkOutput == null) {
            throw new NoSuchLinkException(url);
        }

        Long linkId = linkOutput.getLinkId();

        if (subscriptionRepository.remove(new SubscriptionIdInput(tgChatId, linkId)) == null) {
            throw new NoSuchSubscriptionException(tgChatId, url);
        }

        return linkOutput;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LinkOutput> listAllForChat(Long tgChatId) {

        var subs = subscriptionRepository.findAll();

        return subs.stream()
                .filter(subscription -> subscription.getTgChatId().equals(tgChatId))
                .map(subscription -> {
                    Long linkId = subscription.getLinkId();
                    return linkRepository.findById(linkId);
                })
                .toList();
    }

}
