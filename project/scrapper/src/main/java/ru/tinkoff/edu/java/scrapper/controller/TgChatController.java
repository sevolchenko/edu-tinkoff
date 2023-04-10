package ru.tinkoff.edu.java.scrapper.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;

@Slf4j
@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    @PostMapping("/{id}")
    private void registerChatById(@PathVariable Long id) {
        log.info("Register chat by id {} called", id);

        if (id.equals(0L)) {
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", id));
        }
    }

    @DeleteMapping("/{id}")
    private void deleteChatById(@PathVariable Long id) {
        log.info("Delete chat by id {} called", id);

        if (id.equals(0L)) { // todo: Выброс исключения при отсутствии чата
            throw new NoSuchChatException(String.format("There is no chat with id %d", id));
        }
    }

}
