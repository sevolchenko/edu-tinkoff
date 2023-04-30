package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;

import java.time.OffsetDateTime;

@JsonTypeName("stackoverflow-linkstate")
public record StackOverflowLinkState (
        OffsetDateTime lastActivityDate,

        Integer answerCount

) implements ILinkState {}
