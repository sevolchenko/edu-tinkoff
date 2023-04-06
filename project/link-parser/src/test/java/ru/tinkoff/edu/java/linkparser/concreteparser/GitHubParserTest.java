package ru.tinkoff.edu.java.linkparser.concreteparser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.linkparser.concreteparser.parseresult.GitHubParseResult;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class GitHubParserTest {

    private GitHubParser gitHubParser;

    @BeforeEach
    public void setup() {
        gitHubParser = new GitHubParser();
    }

    @Test
    void testParseValidGitHub() {
        // given
        URI url = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/");

        // when
        var res = gitHubParser.parse(url);

        // then
        var excepted = new GitHubParseResult("sanyarnd", "tinkoff-java-course-2022");
        assertThat(res, is(equalTo(excepted)));
    }

    @Test
    void testParseValidStackOverflow() {
        // given
        URI url = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseInvalidStackOverflow() {
        // given
        URI url = URI.create("https://stackoverflow.com/search?q=unsupported%20link");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseUnsupportedGoogle() {
        // given
        URI url = URI.create("https://google.com");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseUnsupportedLocalHost() {
        // given
        URI url = URI.create("http://localhost:8080");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseValidGitHub2() {
        // given
        URI url = URI.create("https://github.com/sevolchenko/online-store/");

        // when
        var res = gitHubParser.parse(url);

        // then
        var excepted = new GitHubParseResult("sevolchenko", "online-store");
        assertThat(res, is(equalTo(excepted)));
    }

    @Test
    void testParseInvalidLink1() {
        // given
        URI url = URI.create("www.vk.com");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseInvalidLink2() {
        // given
        URI url = URI.create("invalid");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testParseInvalidLink3() {
        // given
        URI url = URI.create("www.github.com");

        // when
        var res = gitHubParser.parse(url);

        // then
        assertThat(res, is(nullValue()));
    }

    @Test
    void testSupportsGitHubLink() {
        // given
        URI url = URI.create("https://github.com/sanyarnd/tinkoff-java-course-2022/");

        // when
        var res = gitHubParser.supports(url);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsStackOverflowLink() {
        // given
        URI url = URI.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c");

        // when
        var res = gitHubParser.supports(url);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsGoogleLink() {
        // given
        URI url = URI.create("https://google.com");

        // when
        var res = gitHubParser.supports(url);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsInvalidLink1() {
        // given
        URI url = URI.create("vk.com");

        // when
        var res = gitHubParser.supports(url);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsInvalidLink2() {
        // given
        URI url = URI.create("invalid");

        // when
        var res = gitHubParser.supports(url);

        // then
        assertThat(res, is(false));
    }

}
