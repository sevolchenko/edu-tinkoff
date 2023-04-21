package ru.tinkoff.edu.java.scrapper.repository.dto.response;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LinkResponse {

    private Long linkId;
    private String url;
    private OffsetDateTime lastScannedAt;
    private OffsetDateTime createdAt;

}
