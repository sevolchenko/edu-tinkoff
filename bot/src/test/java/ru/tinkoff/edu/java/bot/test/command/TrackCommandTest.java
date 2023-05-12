package ru.tinkoff.edu.java.bot.test.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.component.textprovider.TrackCommandTextProvider;
import ru.tinkoff.edu.java.bot.model.service.TestMessageTemplates;
import ru.tinkoff.edu.java.bot.model.telegram.TestChat;
import ru.tinkoff.edu.java.bot.model.telegram.TestMessage;
import ru.tinkoff.edu.java.bot.model.telegram.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.TrackCommand;
import ru.tinkoff.edu.java.bot.util.UrlUtil;
import ru.tinkoff.edu.java.shared.scrapper.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.shared.scrapper.dto.response.LinkResponse;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class TrackCommandTest {

    private TrackCommand trackCommand;

    private IScrapperClient scrapperClient;
    private TrackCommandTextProvider textProvider;

    @BeforeEach
    public void setup() {

        this.scrapperClient = Mockito.mock(IScrapperClient.class);
        this.textProvider = new TrackCommandTextProvider(TestMessageTemplates.get());

        this.trackCommand = new TrackCommand(scrapperClient, textProvider);

    }

    @Test
    void testCommand() {
        // given

        // when
        var res = trackCommand.command();

        // then
        assertThat(res, is(equalTo("/track")));
    }

    @Test
    void testDescription() {
        // given

        // when
        var res = trackCommand.description();

        // then
        assertThat(res, is(equalTo("начать отслеживание ссылки")));
    }


    @Test
    void testSupportsValidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/track"));

        // when
        var res = trackCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsValidCommand2() {
        // doNothing().when(scrapperClient).registerChatById(anyLong());
        // given
        var update = new TestUpdate(new TestMessage("/track aaa"));

        // when
        var res = trackCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsInvalidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/hello"));

        // when
        var res = trackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsAnyMessage() {
        // given
        var update = new TestUpdate(new TestMessage("Any message"));

        // when
        var res = trackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsEmptyMessage() {
        // given
        var update = new TestUpdate(new TestMessage(""));

        // when
        var res = trackCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testHandleCommandWithNoText() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/track", new TestChat(chatId)));


        // when
        var res = trackCommand.handle(update);


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

        var update = new TestUpdate(new TestMessage("/track aaa", new TestChat(chatId)));

        // when
        var res = trackCommand.handle(update);


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

        var update = new TestUpdate(new TestMessage("/track www.google.com", new TestChat(chatId)));

        // when
        var res = trackCommand.handle(update);


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

        var update = new TestUpdate(new TestMessage("/track " + link, new TestChat(chatId)));
        when(scrapperClient.addLink(eq(chatId), ArgumentMatchers.any(AddLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = trackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getSuccessfullyAddedLinkText(link.toString()))));

    }

    @Test
    void testHandleCommandWithValidLink2() {
        // given
        Long chatId = randomId();
        URI link = UrlUtil.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c");

        var update = new TestUpdate(new TestMessage("/track " + link, new TestChat(chatId)));
        when(scrapperClient.addLink(eq(chatId), ArgumentMatchers.any(AddLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = trackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(textProvider.getSuccessfullyAddedLinkText(link.toString()))));

    }

}
