package ru.tinkoff.edu.java.linkparser.concreteparser;

import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;

import java.net.URL;

public final class StackOverflowParser extends ConcreteParser<StackOverflowParseResult> {

    private static final String HOST_NAME = "stackoverflow.com";
    private static final String[] REQUIRED_PARTS = {"questions"};

    public StackOverflowParser(ConcreteParser next) {
        super(next);
    }

    public StackOverflowParser() {
        this(null);
    }

    @Override
    public ParseResult parse(final URL url) {
        if (HOST_NAME.equals(url.getHost())) {
            String[] parts = url.getPath().split("/");
            for (int partIdx = 0; partIdx < REQUIRED_PARTS.length; partIdx++) {
                if (!REQUIRED_PARTS[partIdx].equals(parts[partIdx + 1])) {
                    return null;
                }
            }
            Integer questionId = Integer.parseInt(parts[REQUIRED_PARTS.length + 1]);
            return new StackOverflowParseResult(questionId);
        } else if (next != null){
            return next.parse(url);
        }
        return null;
    }

}
