package ru.tinkoff.edu.java.bot.client.scrapper;


import ru.tinkoff.edu.java.shared.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ListLinkResponse;

public interface IScrapperClient {

    void registerChatById(Long id, String username);

    void deleteChatById(Long id);


    ListLinkResponse getLinks(Long id);

    LinkResponse addLink(Long id, AddLinkRequest link);

    LinkResponse deleteLink(Long id, RemoveLinkRequest link);

}
