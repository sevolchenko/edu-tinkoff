package ru.tinkoff.edu.java.scrapper.client.tgbot.impl;

import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.scrapper.client.tgbot.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.client.tgbot.ITgBotClient;

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
                .bodyValue(linkUpdateRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
