package ru.tinkoff.edu.java.bot.client.scrapper;


import ru.tinkoff.edu.java.bot.client.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;

public interface IScrapperClient {

    void registerChatById(Long id, String username);

    void deleteChatById(Long id);


    ListLinkResponse getLinks(Long id);

    LinkResponse addLink(Long id, AddLinkRequest link);

    LinkResponse deleteLink(Long id, RemoveLinkRequest link);

}
