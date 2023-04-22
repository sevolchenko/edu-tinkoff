package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LinkOutput {

    private Long linkId;
    private String url;
    private OffsetDateTime lastScannedAt;
    private OffsetDateTime createdAt;

}