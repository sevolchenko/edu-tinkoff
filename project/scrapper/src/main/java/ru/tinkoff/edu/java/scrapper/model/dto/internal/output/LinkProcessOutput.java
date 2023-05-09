package ru.tinkoff.edu.java.scrapper.model.dto.internal.output;

import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

public record LinkProcessOutput(
        ILinkState newState,

        LinkEvent event
) {
}
