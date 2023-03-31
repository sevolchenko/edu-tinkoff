package ru.tinkoff.edu.java.bot.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.dto.request.LinkUpdateRequest;

@RestController
@RequestMapping()
@AllArgsConstructor
public class BotController {

    @PostMapping("/updates")
    private void updates(@RequestBody LinkUpdateRequest linkUpdateRequest) {
    }

}
