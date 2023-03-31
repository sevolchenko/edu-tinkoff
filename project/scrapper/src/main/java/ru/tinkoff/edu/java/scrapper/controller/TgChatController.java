package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController {

    @PostMapping("/{id}")
    private void registerChatById(@PathVariable Integer id) {
        if (id.equals(0)) { // todo: Выброс исключения при существовании чата
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", id));
        }
    }

    @DeleteMapping("/{id}")
    private void deleteChatById(@PathVariable Integer id) {
        if (id.equals(0)) { // todo: Выброс исключения при отсутствии чата
            throw new NoSuchChatException(String.format("There is no chat with id %d", id));
        }
    }

}
