package ru.tinkoff.edu.java.bot.client.scrapper.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.scrapper.exception.ScrapperClientException;
import ru.tinkoff.edu.java.bot.client.scrapper.exception.ScrapperServerException;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ApiErrorResponse;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ListLinkResponse;


@Slf4j
public class ScrapperClient implements IScrapperClient {

    private final WebClient webClient;

    public ScrapperClient(String baseUrl) {
        this.webClient = WebClient.builder()
                .filter(errorHandler())
                .baseUrl(baseUrl)
                .build();
    }

    private static ExchangeFilterFunction errorHandler() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if (clientResponse.statusCode().is4xxClientError()) {
                return clientResponse.bodyToMono(ApiErrorResponse.class)
                        .flatMap(apiErrorResponse -> {
                            log.warn(apiErrorResponse.exceptionMessage());
                            return Mono.error(new ScrapperClientException(apiErrorResponse));
                        });
            } else if (clientResponse.statusCode().is5xxServerError()) {
                return clientResponse.bodyToMono(String.class)
                        .flatMap(error -> {
                            log.warn(error);
                            return Mono.error(new ScrapperServerException(error));
                        });
            } else {
                return Mono.just(clientResponse);
            }
        });
    }

    @Override
    public void registerChatById(Long id, String username) {
        log.info("Register chat by id {} and username {} called", id, username);

        String path = "/tg-chat/{id}";
        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(id))
                .bodyValue(username)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public void deleteChatById(Long id) {
        log.info("Delete chat by id {} called", id);

        String path = "/tg-chat/{id}";
        webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .build(id))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    @Override
    public ListLinkResponse getLinks(Long id) {
        log.info("Get links by id {} called", id);

        String path = "/links";
        return webClient.get()
                .uri(path)
                .header("Tg-Chat-Id", String.valueOf(id))
                .retrieve()
                .bodyToMono(ListLinkResponse.class)
                .block();
    }

    @Override
    public LinkResponse addLink(Long id, AddLinkRequest linkRequest) {
        log.info("Add link {} by id {} called", linkRequest.link(), id);

        String path = "/links";
        return webClient.post()
                .uri(path)
                .header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(linkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    @Override
    public LinkResponse deleteLink(Long id, RemoveLinkRequest linkRequest) {
        log.info("Delete link {} by id {} called", linkRequest.link(), id);

        String path = "/links";
        return webClient.method(HttpMethod.DELETE)
                .uri(path)
                .header("Tg-Chat-Id", String.valueOf(id))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(linkRequest)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }
}
