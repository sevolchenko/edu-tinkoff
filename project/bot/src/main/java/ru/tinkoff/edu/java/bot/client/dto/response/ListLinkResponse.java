package ru.tinkoff.edu.java.bot.client.dto.response;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links, Integer size) { }
