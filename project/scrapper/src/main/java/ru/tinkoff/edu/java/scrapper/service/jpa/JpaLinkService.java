package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.exception.AlreadySubscribedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchSubscriptionException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.Link;
import ru.tinkoff.edu.java.scrapper.model.entity.Subscription;
import ru.tinkoff.edu.java.scrapper.model.entity.SubscriptionId;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;

import java.net.URI;
import java.time.Instant;
import java.util.Collection;

@RequiredArgsConstructor
@Transactional
public class JpaLinkService implements ILinkService {

    private final JpaLinkRepository linkRepository;
    private final JpaSubscriptionRepository subscriptionRepository;
    private final JpaTgChatRepository tgChatRepository;

    private final LinkProcessor linkProcessor;

    private final LinkOutputMapper mapper;

    @Override
    public LinkOutput add(Long tgChatId, URI url) {

        TgChat tgChat = tgChatRepository.findById(tgChatId)
                .orElseThrow(() -> new NoSuchChatException(tgChatId));

        Link link = linkRepository.findByUrlEquals(url.toString())
                .orElseGet(() -> {
                    var state = linkProcessor.getState(url);
                    return linkRepository.save(new Link()
                            .setUrl(url.toString())
                            .setState(state)
                            .setCreatedAt(Instant.now())
                            .setLastScannedAt(Instant.now())
                    );
                });

        SubscriptionId subscriptionId = new SubscriptionId()
                .setLink(link)
                .setTgChat(tgChat);

        if (subscriptionRepository.existsBySubscriptionId(subscriptionId)) {
            throw new AlreadySubscribedLinkException(tgChatId, url);
        }

        Subscription sub = new Subscription()
                .setSubscriptionId(subscriptionId)
                .setCreatedAt(Instant.now());

        subscriptionRepository.save(sub);

        return mapper.map(link);
    }

    @Override
    public LinkOutput remove(Long tgChatId, URI url) {
        TgChat tgChat = tgChatRepository.findById(tgChatId)
                .orElseThrow(() -> new NoSuchChatException(tgChatId));

        Link link = linkRepository.findByUrlEquals(url.toString())
                .orElseThrow(() -> new NoSuchLinkException(url));

        SubscriptionId subscriptionId = new SubscriptionId()
                .setLink(link)
                .setTgChat(tgChat);

        if (!subscriptionRepository.existsBySubscriptionId(subscriptionId)) {
            throw new NoSuchSubscriptionException(tgChatId, url);
        }

        subscriptionRepository.deleteById(subscriptionId);

        return mapper.map(link);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LinkOutput> listAllForChat(Long tgChatId) {
        TgChat tgChat = tgChatRepository.findById(tgChatId)
                .orElseThrow(() -> new NoSuchChatException(String.format("There is no chat with id %d", tgChatId)));

        return tgChat.getSubscriptions().stream()
                .map((subscription -> {
                    Link link = subscription.getSubscriptionId().getLink();
                    return mapper.map(link);
                }))
                .toList();
    }
}
