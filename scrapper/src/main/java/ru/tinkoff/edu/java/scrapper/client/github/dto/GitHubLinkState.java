package ru.tinkoff.edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;
import ru.tinkoff.edu.java.shared.scrapper.event.LinkEvent;

import java.time.OffsetDateTime;

@JsonTypeName("github-linkstate")
public record GitHubLinkState(

        OffsetDateTime pushedAt,

        Integer branchesCount

) implements ILinkState {

    @Override
    public LinkEvent compareTo(ILinkState newState) {

        GitHubLinkState gitHubLinkState;

        if (newState instanceof GitHubLinkState) {
            gitHubLinkState = (GitHubLinkState) newState;
        } else {
            return null;
        }

        if (gitHubLinkState.branchesCount() > this.branchesCount()) {
            return LinkEvent.BRANCHES_COUNT_INCREASED;
        }
        if (gitHubLinkState.branchesCount() < this.branchesCount()) {
            return LinkEvent.BRANCHES_COUNT_DECREASED;
        }

        if (gitHubLinkState.pushedAt().isAfter(this.pushedAt())) {
            return LinkEvent.UPDATED;
        }

        return null;
    }

}
