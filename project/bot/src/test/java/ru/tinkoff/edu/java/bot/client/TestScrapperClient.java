package ru.tinkoff.edu.java.bot.client;

import ru.tinkoff.edu.java.bot.client.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.client.dto.response.ListLinkResponse;

public class TestScrapperClient implements IScrapperClient {

    @Override
    public void registerChatById(Long id) {
    }

    @Override
    public void deleteChatById(Long id) {
    }

    @Override
    public ListLinkResponse getLinks(Long id) {
        return new ListLinkResponse(null, null);
    }

    @Override
    public LinkResponse addLink(Long id, AddLinkRequest link) {
        return new LinkResponse(null, null);
    }

    @Override
    public LinkResponse deleteLink(Long id, RemoveLinkRequest link) {
        return new LinkResponse(null, null);
    }

}
