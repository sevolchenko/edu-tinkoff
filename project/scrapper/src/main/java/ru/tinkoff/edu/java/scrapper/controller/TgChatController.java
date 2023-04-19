package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ITgChatService;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    private final ITgChatService tgChatService;

    @PostMapping("/{id}")
    private void registerChatById(@PathVariable Long id, @RequestBody String username) {
        log.info("Register chat by id {} and username {} called", id, username);

        tgChatService.register(new RegisterTgChatRequest(id, username));
    }

    @DeleteMapping("/{id}")
    private void deleteChatById(@PathVariable Long id) {
        log.info("Delete chat by id {} called", id);

        tgChatService.unregister(id);
    }

}
