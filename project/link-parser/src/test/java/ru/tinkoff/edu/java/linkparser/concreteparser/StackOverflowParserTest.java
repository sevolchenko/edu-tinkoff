package ru.tinkoff.edu.java.linkparser.concreteparser;

import org.junit.Assert;
import org.junit.Test;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;

import java.net.URI;

public class StackOverflowParserTest {

    @Test
    public void test1() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/");
        ParseResult res = stackOverflowParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test2() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        ParseResult res = stackOverflowParser.parse(url);

        ParseResult excepted = new StackOverflowParseResult(1642028);

        Assert.assertEquals(excepted, res);
    }

    @Test
    public void test3() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("https://stackoverflow.com/search?q=unsupported%20link");
        ParseResult res = stackOverflowParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test4() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("https://google.com");
        ParseResult res = stackOverflowParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test5() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("http://localhost:8080");
        ParseResult res = stackOverflowParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test6() {
        StackOverflowParser stackOverflowParser = new StackOverflowParser();

        URI url = URI.create("https://github.com/sevolchenko/online-store/");
        ParseResult res = stackOverflowParser.parse(url);

        Assert.assertNull(res);
    }

}
