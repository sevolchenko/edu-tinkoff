package ru.tinkoff.edu.java.scrapper.client.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.scrapper.client.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.dto.request.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionResponse;
import ru.tinkoff.edu.java.scrapper.client.dto.response.StackOverflowQuestionsResponse;

import java.util.Objects;

@RequiredArgsConstructor
public class StackOverflowClient implements IStackOverflowClient {

    private static final String API_URL = "https://api.stackexchange.com/2.3";

    private final WebClient webClient;

    public static StackOverflowClient create() {
        return create(API_URL);
    }

    public static StackOverflowClient create(String baseUrl) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        return new StackOverflowClient(webClient);
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
        Objects.requireNonNull(responseBody);
        return responseBody;
    }

}
