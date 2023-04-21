package ru.tinkoff.edu.java.scrapper.client.impl;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.ITgBotClient;
import ru.tinkoff.edu.java.scrapper.client.dto.request.LinkUpdateRequest;

public class TgBotClient implements ITgBotClient {

    private final WebClient webClient;

    public TgBotClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public void postUpdates(LinkUpdateRequest linkUpdateRequest) {
        String path = "/updates";
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build())
                .retrieve();
    }

}
