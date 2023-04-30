package ru.tinkoff.edu.java.scrapper.component.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubLinkState;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowLinkState;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.exception.InvalidLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NotSupportedLinkException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkEvent;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkProcessOutput;

import java.net.URI;

@Component
@RequiredArgsConstructor
public class LinkProcessor {

    private final LinkParser linkParser;

    private final IGitHubClient gitHubClient;
    private final IStackOverflowClient stackOverflowClient;

    public LinkProcessOutput processLink(String url, ILinkState linkState) {

        var parseResult = linkParser.parse(URI.create(url));

        ILinkState clientState = null;
        LinkEvent event = null;

        switch (parseResult) {
            case GitHubParseResult gitHub -> {
                GitHubRepositoryResponse response = gitHubClient.fetchRepository(new GitHubRepositoryRequest(gitHub.username(), gitHub.repository()));

                var initialState = (GitHubLinkState) linkState;

                var fetchedState = response.state();
                clientState = fetchedState;

                if (fetchedState.branchesCount() > initialState.branchesCount()) {
                    event = LinkEvent.BRANCHES_COUNT_INCREASED;
                    break;
                }
                if (fetchedState.branchesCount() < initialState.branchesCount()) {
                    event = LinkEvent.BRANCHES_COUNT_DECREASED;
                    break;
                }

                if (fetchedState.pushedAt().isAfter(initialState.pushedAt())) {
                    event = LinkEvent.UPDATED;
                }

            }
            case StackOverflowParseResult stackOverflow -> {
                var response = stackOverflowClient.fetchQuestion(new StackOverflowQuestionRequest(stackOverflow.questionId()));

                var initialState = (StackOverflowLinkState) linkState;

                var fetchedState = response.state();
                clientState = fetchedState;

                if (fetchedState.answerCount() > initialState.answerCount()) {
                    event = LinkEvent.ANSWERS_COUNT_INCREASED;
                    break;
                }

                if (fetchedState.lastActivityDate().isAfter(initialState.lastActivityDate())) {
                    event = LinkEvent.UPDATED;
                }
            }
        }

        return new LinkProcessOutput(clientState, event);

    }

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
                GitHubRepositoryResponse response = gitHubClient.fetchRepository(new GitHubRepositoryRequest(gitHub.username(), gitHub.repository()));

                clientState = response.state();

            }
            case StackOverflowParseResult stackOverflow -> {
                var response = stackOverflowClient.fetchQuestion(new StackOverflowQuestionRequest(stackOverflow.questionId()));

                clientState = response.state();
            }
        }

        return clientState;
    }

}
