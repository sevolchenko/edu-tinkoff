package ru.tinkoff.edu.java.linkparser;


import ru.tinkoff.edu.java.linkparser.concreteparser.GitHubParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.StackOverflowParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;

import java.net.URI;

public class LinkParser {

    private final GitHubParser gitHubParser;
    private final StackOverflowParser stackOverflowParser;

    public LinkParser() {
        this.gitHubParser = new GitHubParser();
        this.stackOverflowParser = new StackOverflowParser(this.gitHubParser);
    }

    public ParseResult parse(final URI url) {
        try {
            return stackOverflowParser.parse(url);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }

    }

    public boolean supports(final URI url) {
        return gitHubParser.supports(url) || stackOverflowParser.supports(url);
    }

}
