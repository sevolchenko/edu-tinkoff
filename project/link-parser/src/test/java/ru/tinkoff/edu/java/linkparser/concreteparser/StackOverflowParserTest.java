package ru.tinkoff.edu.java.linkparser.concreteparser;

import org.junit.Assert;
import org.junit.Test;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.ParseResult;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.StackOverflowParseResult;

import java.net.MalformedURLException;
import java.net.URL;

public class StackOverflowParserTest {

    @Test
    public void test1() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("https://github.com/sanyarnd/tinkoff-java-course-2022/");
            ParseResult res = stackOverflowParser.parse(url);

            Assert.assertNull(res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");
            ParseResult res = stackOverflowParser.parse(url);

            ParseResult excepted = new StackOverflowParseResult(1642028);

            Assert.assertEquals(excepted, res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("https://stackoverflow.com/search?q=unsupported%20link");
            ParseResult res = stackOverflowParser.parse(url);

            Assert.assertNull(res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test4() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("https://google.com");
            ParseResult res = stackOverflowParser.parse(url);

            Assert.assertNull(res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test5() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("http://localhost:8080");
            ParseResult res = stackOverflowParser.parse(url);

            Assert.assertNull(res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test6() {
        try {
            StackOverflowParser stackOverflowParser = new StackOverflowParser();

            URL url = new URL("https://github.com/sevolchenko/online-store/");
            ParseResult res = stackOverflowParser.parse(url);

            Assert.assertNull(res);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

}
