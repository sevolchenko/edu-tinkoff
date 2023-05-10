package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class SubscriptionOutput {

    private Long tgChatId;
    private Long linkId;
    private OffsetDateTime createdAt;

}
