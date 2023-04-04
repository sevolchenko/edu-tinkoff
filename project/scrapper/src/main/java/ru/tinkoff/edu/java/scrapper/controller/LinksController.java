package ru.tinkoff.edu.java.scrapper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyAddedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;

@Slf4j
@RestController
@RequestMapping("/links")
public class LinksController {

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        log.info(String.format("Get Links by id %d called", id));

        return new ListLinkResponse(null, null);
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long id,
                                @RequestBody AddLinkRequest request) {
        log.info(String.format("Add Link %s by id %d called", request.link(), id));

        // todo: Интеграция с LinkParser в слое сервисов
        if (request.link().toString().equals("exists")) { // todo: Выброс исключения, если ссылка уже отслеживается
            throw new AlreadyAddedLinkException(String.format("Link already added to id %d: %s", id, request.link()));
        }
        return new LinkResponse(null, null);
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") Long id,
                                @RequestBody RemoveLinkRequest request) {
        log.info(String.format("Delete Link %s by id %d called", request.link(), id));

        if (request.link().toString().equals("doesnt exists")) { // todo: Выброс исключения, если ссылка не отслеживается
            throw new NoSuchLinkException(String.format("Link has not added to id %d yet: %s", id, request.link()));
        }
        return new LinkResponse(null, null);
    }
}
