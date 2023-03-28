package ru.tinkoff.edu.java.linkparser;


import ru.tinkoff.edu.java.linkparser.concreteparser.GitHubParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.StackOverflowParser;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;

import java.net.URI;

public class LinkParser {

    public ParseResult parse(final URI url) {
        GitHubParser gitHubParser = new GitHubParser();
        StackOverflowParser stackOverflowParser = new StackOverflowParser(gitHubParser);

        return stackOverflowParser.parse(url);
    }

}