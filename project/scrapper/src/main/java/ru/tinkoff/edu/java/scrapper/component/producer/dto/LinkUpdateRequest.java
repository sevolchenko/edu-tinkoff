package ru.tinkoff.edu.java.scrapper.component.producer.dto;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(Long linkId,
                                URI url,
                                Integer eventCode,
                                List<Long> tgChatIds)
        implements Serializable {
}
