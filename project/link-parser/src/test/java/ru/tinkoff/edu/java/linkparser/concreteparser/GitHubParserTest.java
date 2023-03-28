package ru.tinkoff.edu.java.linkparser.concreteparser;

import org.junit.Assert;
import org.junit.Test;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;

import java.net.URI;

public class GitHubParserTest {

    @Test
    public void test1() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/");
        ParseResult res = gitHubParser.parse(url);

        ParseResult excepted = new GitHubParseResult("sanyarnd", "tinkoff-java-course-2022");

        Assert.assertEquals(excepted, res);
    }

    @Test
    public void test2() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
        ParseResult res = gitHubParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test3() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("https://stackoverflow.com/search?q=unsupported%20link");
        ParseResult res = gitHubParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test4() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("https://google.com");
        ParseResult res = gitHubParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test5() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("http://localhost:8080");
        ParseResult res = gitHubParser.parse(url);

        Assert.assertNull(res);
    }

    @Test
    public void test6() {
        GitHubParser gitHubParser = new GitHubParser();

        URI url = URI.create("https://github.com/sevolchenko/online-store/");
        ParseResult res = gitHubParser.parse(url);

        ParseResult excepted = new GitHubParseResult("sevolchenko", "online-store");

        Assert.assertEquals(excepted, res);
    }

}
