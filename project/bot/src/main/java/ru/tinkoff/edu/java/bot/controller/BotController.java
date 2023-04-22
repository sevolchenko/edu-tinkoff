package ru.tinkoff.edu.java.bot.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkUpdateRequest;

@RestController
public class BotController {

    @PostMapping("/updates")
    private void updates(@RequestBody LinkUpdateRequest linkUpdateRequest) {

    }

}
