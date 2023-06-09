package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkService;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.ListLinkResponse;

import java.net.URI;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/links")
public class LinksController {

    private final ILinkService linkService;

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") Long id) {
        log.info("Get Links by id {} called", id);

        var collection = linkService.listAllForChat(id);

        var resList = collection.stream()
                .map(linkResponse -> new LinkResponse(linkResponse.getLinkId(), URI.create(linkResponse.getUrl())))
                .toList();

        return new ListLinkResponse(resList, resList.size());
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Long id,
                                @RequestBody AddLinkRequest request) {
        log.info("Add Link {} by id {} called", request.link(), id);

        var response = linkService.add(id, request.link());

        return new LinkResponse(response.getLinkId(), URI.create(response.getUrl()));
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") Long id,
                                   @RequestBody RemoveLinkRequest request) {
        log.info("Delete Link {} by id {} called", request.link(), id);

        var response = linkService.remove(id, request.link());

        return new LinkResponse(response.getLinkId(), URI.create(response.getUrl()));
    }
}
