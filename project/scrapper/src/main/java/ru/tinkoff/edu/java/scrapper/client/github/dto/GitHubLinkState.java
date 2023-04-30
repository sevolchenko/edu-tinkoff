package ru.tinkoff.edu.java.scrapper.client.github.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;

import java.time.OffsetDateTime;

@JsonTypeName("github-linkstate")
public record GitHubLinkState (

        OffsetDateTime pushedAt,

        Integer branchesCount

) implements ILinkState {}
