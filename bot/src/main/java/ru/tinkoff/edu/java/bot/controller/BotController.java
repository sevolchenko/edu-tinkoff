package ru.tinkoff.edu.java.bot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.service.bot.INotificationService;
import ru.tinkoff.edu.java.shared.bot.request.LinkUpdateRequest;

@RestController
@RequiredArgsConstructor
public class BotController {

    private final INotificationService notificationService;

    @PostMapping("/updates")
    private void updates(@RequestBody LinkUpdateRequest linkUpdateRequest) {

        notificationService.linkUpdateReceived(linkUpdateRequest.url(),
                linkUpdateRequest.event(), linkUpdateRequest.tgChatIds());


    }

}
