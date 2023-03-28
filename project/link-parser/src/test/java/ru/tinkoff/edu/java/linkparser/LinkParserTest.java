package ru.tinkoff.edu.java.linkparser;

import org.junit.Assert;
import org.junit.Test;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;

import java.net.URI;

public class LinkParserTest {

    @Test
    public void test1() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/");
        ParseResult res = linkParser.parse(url);

        ParseResult excepted = new GitHubParseResult("sanyarnd", "tinkoff-java-course-2022");

        Assert.assertEquals(excepted, res);
    }

    @Test
    public void test2() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        ParseResult res = linkParser.parse(url);

        ParseResult excepted = new StackOverflowParseResult(1642028);

        Assert.assertEquals(excepted, res);
    }

    @Test
    public void test3() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("https://stackoverflow.com/search?q=unsupported%20link");
        ParseResult res = linkParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test4() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("https://google.com");
        ParseResult res = linkParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test5() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("http://localhost:8080");
        ParseResult res = linkParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test6() {
        LinkParser linkParser = new LinkParser();

        URI url = URI.create("https://github.com/sevolchenko/online-store/");
        ParseResult res = linkParser.parse(url);

        ParseResult excepted = new GitHubParseResult("sevolchenko", "online-store");

        Assert.assertEquals(excepted, res);
    }

}
