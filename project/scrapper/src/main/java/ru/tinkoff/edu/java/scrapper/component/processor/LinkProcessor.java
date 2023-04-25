package ru.tinkoff.edu.java.scrapper.component.processor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.linkparser.LinkParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;
import ru.tinkoff.edu.java.scrapper.client.github.IGitHubClient;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubRepositoryRequest;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.IStackOverflowClient;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionRequest;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.LinkProcessInput;

import java.net.URI;
import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class LinkProcessor {

    private final LinkParser linkParser;

    private final IGitHubClient gitHubClient;
    private final IStackOverflowClient stackOverflowClient;

    public boolean processLink(String url, LinkProcessInput linkProcessInput) {

        var parseResult = linkParser.parse(URI.create(url));

        OffsetDateTime clientUpdatedAt = null;

        switch (parseResult) {
            case GitHubParseResult gitHub -> {
                var response = gitHubClient.fetchRepository(new GitHubRepositoryRequest(gitHub.username(), gitHub.repository()));

                clientUpdatedAt = response.updatedAt();

            }
            case StackOverflowParseResult stackOverflow -> {
                var response = stackOverflowClient.fetchQuestion(new StackOverflowQuestionRequest(stackOverflow.questionId()));

                clientUpdatedAt = response.lastActivityDate();
            }
        }

        return linkProcessInput.lastScannedAt().isBefore(clientUpdatedAt);

    }

}
