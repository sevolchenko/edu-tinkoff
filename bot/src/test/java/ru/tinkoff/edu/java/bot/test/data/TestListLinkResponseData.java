package ru.tinkoff.edu.java.bot.test.data;

import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ListLinkResponse;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TestListLinkResponseData {

    private static final Random RND = new Random();

    public static ListLinkResponse stabValidResponse(Long chatId) {
        return buildListResponse(
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com"))
        );
    }

    public static ListLinkResponse stabValidBigResponse(Long chatId) {
        return buildListResponse(
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com")),
                buildResponse(chatId, URI.create("https://vk.com")),
                buildResponse(chatId, URI.create("https://google.com")),
                buildResponse(chatId, URI.create("https://stackoverflow.com"))
        );
    }

    public static ListLinkResponse stabEmptyResponse() {
        return buildListResponse();
    }

    public static ListLinkResponse buildListResponse(List<LinkResponse> links) {
        return new ListLinkResponse(links, links.size());
    }

    public static ListLinkResponse buildListResponse(LinkResponse... links) {
        return buildListResponse(Arrays.asList(links));
    }

    public static LinkResponse buildResponse(Long id, URI url) {
        return new LinkResponse(id, url);
    }

    public static Long randomId() {
        return RND.nextLong(Long.MAX_VALUE);
    }

}
