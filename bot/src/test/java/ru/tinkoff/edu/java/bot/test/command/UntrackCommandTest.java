package ru.tinkoff.edu.java.bot.test.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.bot.client.scrapper.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.component.textprovider.UntrackCommandTextProvider;
import ru.tinkoff.edu.java.bot.model.service.TestMessageTemplates;
import ru.tinkoff.edu.java.bot.model.telegram.TestChat;
import ru.tinkoff.edu.java.bot.model.telegram.TestMessage;
import ru.tinkoff.edu.java.bot.model.telegram.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.UntrackCommand;
import ru.tinkoff.edu.java.bot.util.UrlUtil;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class UntrackCommandTest {

    private UntrackCommand untrackCommand;

    private IScrapperClient scrapperClient;
    private UntrackCommandTextProvider textProvider;

    @BeforeEach
    public void setup() {
        this.scrapperClient = Mockito.mock(IScrapperClient.class);
        this.textProvider = new UntrackCommandTextProvider(TestMessageTemplates.get());

        this.untrackCommand = new UntrackCommand(scrapperClient, textProvider);

    }

    @Test
    void testCommand() {
        // given

        // when
        var res = untrackCommand.command();

        // then
        assertThat(res, is(equalTo("/untrack")));
    }

    @Test
    void testDescription() {
        // given

        // when
        var res = untrackCommand.description();

        // then
        assertThat(res, is(equalTo("прекратить отслеживание ссылки")));
    }


    @Test
    void testSupportsValidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/untrack"));

        // when
        var res = untrackCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsValidCommand2() {
        // doNothing().when(scrapperClient).registerChatById(anyLong());
        // given
        var update = new TestUpdate(new TestMessage("/untrack aaa"));

        // when
        var res = untrackCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsInvalidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/hello"));

        // when
        var res = untrackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsAnyMessage() {
        // given
        var update = new TestUpdate(new TestMessage("Any message"));

        // when
        var res = untrackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsEmptyMessage() {
        // given
        var update = new TestUpdate(new TestMessage(""));

        // when
        var res = untrackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testHandleCommandWithNoText() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/untrack", new TestChat(chatId)));


        // when
        var res = untrackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getNoLinkErrorText())));

    }


    @Test
    void testHandleCommandWithInvalidLink1() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/untrack aaa", new TestChat(chatId)));

        // when
        var res = untrackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getInvalidLinkText())));

    }

    @Test
    void testHandleCommandWithInvalidLink2() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/untrack www.google.com", new TestChat(chatId)));

        // when
        var res = untrackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getInvalidLinkText())));

    }


    @Test
    void testHandleCommandWithValidLink1() {
        // given
        Long chatId = randomId();
        URI link = UrlUtil.create("https://github.com/sevolchenko/online-store");

        var update = new TestUpdate(new TestMessage("/untrack " + link, new TestChat(chatId)));
        when(scrapperClient.deleteLink(eq(chatId), ArgumentMatchers.any(RemoveLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = untrackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getSuccessfullyRemovedLinkText(link.toString()))));

    }

    @Test
    void testHandleCommandWithValidLink2() {
        // given
        Long chatId = randomId();
        URI link = UrlUtil.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c");

        var update = new TestUpdate(new TestMessage("/untrack " + link, new TestChat(chatId)));
        when(scrapperClient.deleteLink(eq(chatId), ArgumentMatchers.any(RemoveLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = untrackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getSuccessfullyRemovedLinkText(link.toString()))));

    }


}
