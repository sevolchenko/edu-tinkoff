package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import lombok.Data;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

import java.time.OffsetDateTime;

@Data
public class LinkOutput {

    private Long linkId;
    private String url;
    private ILinkState state;
    private OffsetDateTime lastScannedAt;
    private OffsetDateTime createdAt;

}
