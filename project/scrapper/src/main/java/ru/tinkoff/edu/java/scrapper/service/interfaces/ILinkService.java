package ru.tinkoff.edu.java.scrapper.service.interfaces;

import ru.tinkoff.edu.java.scrapper.repository.dto.response.LinkResponse;

import java.net.URI;
import java.util.Collection;

public interface ILinkService {

    LinkResponse add(Long tgChatId, URI url);
    LinkResponse remove(Long tgChatId, URI url);

    Collection<LinkResponse> listAll();
    Collection<LinkResponse> listAllForChat(Long tgChatId);
}
