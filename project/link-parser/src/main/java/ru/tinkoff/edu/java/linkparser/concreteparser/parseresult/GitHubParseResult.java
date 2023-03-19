package ru.tinkoff.edu.java.linkparser.concreteparser.parseresult;

public record GitHubParseResult(String username, String repository) implements ParseResult {}
