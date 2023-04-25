package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import java.time.OffsetDateTime;
import java.util.List;

public record LinkWithChatsOutput(Long linkId,
                                  String url,

                                  OffsetDateTime lastScannedAt,
                                  List<Long> tgChatIds) {}
