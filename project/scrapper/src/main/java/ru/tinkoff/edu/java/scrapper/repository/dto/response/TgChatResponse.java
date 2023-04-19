package ru.tinkoff.edu.java.scrapper.repository.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TgChatResponse {

    private Long tgChatId;
    private String username;
    private OffsetDateTime registeredAt;

}
