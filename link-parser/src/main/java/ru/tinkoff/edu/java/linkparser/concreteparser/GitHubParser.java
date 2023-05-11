package ru.tinkoff.edu.java.linkparser.concreteparser;

import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;

import java.net.URI;

public final class GitHubParser extends ConcreteParser<GitHubParseResult> {

    private static final String HOST_NAME = "github.com";

    public GitHubParser(ConcreteParser next) {
        super(next);
    }

    public GitHubParser() {
        this(null);
    }

    @Override
    public boolean supports(URI url) {
        return HOST_NAME.equals(url.getHost());
    }

    @Override
    public ParseResult parse(URI url) {
        if (supports(url)) {
            String[] parts = url.getPath().split("/");
            String username = parts[1];
            String repository = parts[2];
            return new GitHubParseResult(username, repository);
        } else if (next != null) {
            return next.parse(url);
        }
        return null;
    }

}
