package ru.tinkoff.edu.java.scrapper.model.linkstate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubLinkState;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowLinkState;
import ru.tinkoff.edu.java.scrapper.util.ObjectMapperUtil;
import ru.tinkoff.edu.java.shared.scrapper.event.LinkEvent;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GitHubLinkState.class, name = "github-linkstate"),
        @JsonSubTypes.Type(value = StackOverflowLinkState.class, name = "stackoverflow-linkstate")
})
public interface ILinkState {

    LinkEvent compareTo(ILinkState newState);

    default String asJson() {
        return ObjectMapperUtil.writeValueAsString(this);
    }

}
