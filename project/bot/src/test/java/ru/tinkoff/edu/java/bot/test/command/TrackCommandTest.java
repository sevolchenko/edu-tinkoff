package ru.tinkoff.edu.java.bot.test.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.client.dto.request.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.response.LinkResponse;
import ru.tinkoff.edu.java.bot.model.TestChat;
import ru.tinkoff.edu.java.bot.model.TestMessage;
import ru.tinkoff.edu.java.bot.model.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.TrackCommand;
import ru.tinkoff.edu.java.bot.util.UrlUtils;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class TrackCommandTest {

    private TrackCommand trackCommand;

    private IScrapperClient scrapperClient;

    @BeforeEach
    public void setup() {

        this.scrapperClient = Mockito.mock(IScrapperClient.class);
        this.trackCommand = new TrackCommand(scrapperClient);

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
        assertThat(text, is(equalTo("После команды через пробел нужно указать ссылку, которую нужно отследить")));

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
        assertThat(text, is(equalTo("Ссылка указана неверно")));

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
        assertThat(text, is(equalTo("Ссылка указана неверно")));

    }


    @Test
    void testHandleCommandWithValidLink1() {
        // given
        Long chatId = randomId();
        URI link = UrlUtils.create("https://github.com/sevolchenko/online-store");

        var update = new TestUpdate(new TestMessage("/track " + link.toString(), new TestChat(chatId)));
        when(scrapperClient.addLink(eq(chatId), ArgumentMatchers.any(AddLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = trackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(String.format("Ссылка %s успешно добавлена к отслеживаемым!\n" +
                "Мы пришлем уведомление, когда по адресу произойдут изменения :)", link))));

    }

    @Test
    void testHandleCommandWithValidLink2() {
        // given
        Long chatId = randomId();
        URI link = UrlUtils.create("https://stackoverflow.com/questions/1642028/what-is-the-operator-in-c-c");

        var update = new TestUpdate(new TestMessage("/track " + link.toString(), new TestChat(chatId)));
        when(scrapperClient.addLink(eq(chatId), ArgumentMatchers.any(AddLinkRequest.class)))
                .thenReturn(new LinkResponse(chatId, link));


        // when
        var res = trackCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, is(equalTo(String.format("Ссылка %s успешно добавлена к отслеживаемым!\n" +
                "Мы пришлем уведомление, когда по адресу произойдут изменения :)", link))));

    }

}