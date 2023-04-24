package ru.tinkoff.edu.java.scrapper.client.stackoverflow.impl;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionListResponse;

public class StackOverflowClient implements IStackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    public StackOverflowQuestionResponse fetchQuestion(StackOverflowQuestionRequest stackOverflowQuestionRequest) {
        String path = "/questions/{id}";
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("site", "stackoverflow")
                        .build(stackOverflowQuestionRequest.id()))
                .retrieve()
                .bodyToMono(StackOverflowQuestionListResponse.class)
                .flatMap(response -> Mono.justOrEmpty(response.items().stream().findFirst()))
                .block();
    }

}
