package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TgChatOutput {

    private Long tgChatId;
    private String username;
    private OffsetDateTime registeredAt;

}
