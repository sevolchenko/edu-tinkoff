package ru.tinkoff.edu.java.scrapper.client.impl;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.dto.request.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionsResponse;

import java.util.Objects;

public class StackOverflowClient implements IStackOverflowClient {

    private final WebClient webClient;

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


        public StackOverflowQuestionResponse fetchQuestion(StackOverflowQuestionRequest stackOverflowQuestionRequest) {
        String path = "/questions/{id}";
        StackOverflowQuestionResponse responseBody = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("site", "stackoverflow")
                        .build(stackOverflowQuestionRequest.id()))
                .retrieve()
                .bodyToMono(StackOverflowQuestionsResponse.class)
                .flatMap(response -> Mono.justOrEmpty(response.items().stream().findFirst()))
                .block();
        return responseBody;
    }

}
