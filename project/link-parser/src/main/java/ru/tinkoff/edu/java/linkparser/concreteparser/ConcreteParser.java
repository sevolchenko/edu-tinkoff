package ru.tinkoff.edu.java.linkparser.concreteparser;

import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;

import java.net.URI;

public sealed abstract class ConcreteParser<R extends ParseResult> permits GitHubParser, StackOverflowParser {

    ConcreteParser<R> next;

    public ConcreteParser(ConcreteParser next) {
        this.next = next;
    }

    public ConcreteParser() {
        this(null);
    }

    public abstract ParseResult parse(final URI url);

}
