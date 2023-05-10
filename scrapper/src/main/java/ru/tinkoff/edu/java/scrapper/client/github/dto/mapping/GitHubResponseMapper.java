package ru.tinkoff.edu.java.scrapper.client.github.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryAPIResponse;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;

@Mapper(componentModel = "spring")
public interface GitHubResponseMapper {

    @Mapping(target = "state.pushedAt", source = "pushedAt")
    @Mapping(target = "state.branchesCount", source = "branchesCount")
    GitHubRepositoryResponse from(GitHubRepositoryAPIResponse apiResponse);

}
