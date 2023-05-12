package ru.tinkoff.edu.java.shared.scrapper.dto.response;

import java.util.List;

public record ListLinkResponse(List<LinkResponse> links, Integer size) {
}
