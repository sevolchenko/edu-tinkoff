package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;
import ru.tinkoff.edu.java.shared.scrapper.event.LinkEvent;

import java.time.OffsetDateTime;

@JsonTypeName("stackoverflow-linkstate")
public record StackOverflowLinkState(
        OffsetDateTime lastActivityDate,

        Integer answerCount

) implements ILinkState {

    @Override
    public LinkEvent compareTo(ILinkState newState) {

        StackOverflowLinkState stackOverflowLinkState;

        if (newState instanceof StackOverflowLinkState) {
            stackOverflowLinkState = (StackOverflowLinkState) newState;
        } else {
            return null;
        }

        if (stackOverflowLinkState.answerCount() > this.answerCount()) {
            return LinkEvent.ANSWERS_COUNT_INCREASED;
        }

        if (stackOverflowLinkState.lastActivityDate().isAfter(this.lastActivityDate())) {
            return LinkEvent.UPDATED;
        }

        return null;

    }

}
