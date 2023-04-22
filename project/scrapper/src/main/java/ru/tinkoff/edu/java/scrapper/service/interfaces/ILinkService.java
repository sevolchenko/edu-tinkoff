package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;

import java.net.URI;
import java.util.Collection;

public interface ILinkService {

    LinkOutput add(Long tgChatId, URI url);
    LinkOutput remove(Long tgChatId, URI url);
    Collection<LinkOutput> listAllForChat(Long tgChatId);
}
