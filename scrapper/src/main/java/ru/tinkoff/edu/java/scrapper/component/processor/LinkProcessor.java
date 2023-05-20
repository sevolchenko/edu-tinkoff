package ru.tinkoff.edu.java.scrapper.component.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.exception.InvalidLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NotSupportedLinkException;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LinkProcessor {

    private final LinkParser linkParser;

    private final IGitHubClient gitHubClient;
    private final IStackOverflowClient stackOverflowClient;

    public ILinkState getState(URI url) {

        if (!linkParser.supports(url)) {
            throw new NotSupportedLinkException(url);
        }

        var parseResult = linkParser.parse(url);

        if (parseResult == null) {
            throw new InvalidLinkException(url);
        }

        ILinkState clientState = null;

        switch (parseResult) {
            case GitHubParseResult gitHub -> {
                var response = gitHubClient.fetchRepository(
                    new GitHubRepositoryRequest(gitHub.username(), gitHub.repository()));

                clientState = response.state();

            }
            case StackOverflowParseResult stackOverflow -> {
                var response = stackOverflowClient.fetchQuestion(
                    new StackOverflowQuestionRequest(stackOverflow.questionId()));

                clientState = response.state();
            }
        }

        return clientState;
    }


}
