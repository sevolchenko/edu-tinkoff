package ru.tinkoff.edu.java.scrapper.client.stackoverflow.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionListResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.mapping.StackOverflowResponseMapper;
import ru.tinkoff.edu.java.scrapper.exception.NotFoundLinkException;

public class StackOverflowClient implements IStackOverflowClient {

    private final WebClient webClient;

    @Autowired
    private StackOverflowResponseMapper mapper;

    public StackOverflowClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }


    @Override
    public StackOverflowQuestionResponse fetchQuestion(StackOverflowQuestionRequest stackOverflowQuestionRequest) {
        String path = "/questions/{id}";
        var apiResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("site", "stackoverflow")
                        .build(stackOverflowQuestionRequest.id()))
                .retrieve()
                .bodyToMono(StackOverflowQuestionListResponse.class)
                .flatMap(response -> Mono.justOrEmpty(response.items().stream().findFirst()))
                .block();
        if (apiResponse == null) {
            throw new NotFoundLinkException("Stackoverflow question %d not found".formatted(
                    stackOverflowQuestionRequest.id()));
        }
        return mapper.map(apiResponse);
    }

}
