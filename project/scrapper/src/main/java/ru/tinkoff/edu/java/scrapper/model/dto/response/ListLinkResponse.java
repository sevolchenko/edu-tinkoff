package ru.tinkoff.edu.java.scrapper.model.dto.response;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links, Integer size) { }
