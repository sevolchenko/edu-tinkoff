package ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubLinkState;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowLinkState;
import ru.tinkoff.edu.java.scrapper.util.ObjectMapperUtil;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
        @JsonSubTypes.Type(value = GitHubLinkState.class, name = "github-linkstate"),
        @JsonSubTypes.Type(value = StackOverflowLinkState.class, name = "stackoverflow-linkstate")
})
public interface ILinkState {

    default String asJson() {
        return ObjectMapperUtil.writeValueAsString(this);
    }

}
