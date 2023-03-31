package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.ListLinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.response.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyAddedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;

@RestController
@RequestMapping("/links")
public class LinksController {

    @GetMapping
    public ListLinkResponse getLinks(@RequestHeader("Tg-Chat-Id") Integer id) {
        return new ListLinkResponse(null, null);
    }

    @PostMapping
    public LinkResponse addLink(@RequestHeader("Tg-Chat-Id") Integer id,
                                @RequestBody AddLinkRequest link) {
        // todo: Проверить ссылку на валидность
        if (link.equals("exists")) { // todo: Выброс исключения, если ссылка уже отслеживается
            throw new AlreadyAddedLinkException(String.format("Link already added: %s", link));
        }
        return new LinkResponse(null, null);
    }

    @DeleteMapping
    public LinkResponse deleteLink(@RequestHeader("Tg-Chat-Id") Integer id,
                                @RequestBody RemoveLinkRequest link) {
        if (link.equals("doesnt exists")) { // todo: Выброс исключения, если ссылка не отслеживается
            throw new NoSuchLinkException(String.format("Link has not added yet: %s", link));
        }
        return new LinkResponse(null, null);
    }
}
