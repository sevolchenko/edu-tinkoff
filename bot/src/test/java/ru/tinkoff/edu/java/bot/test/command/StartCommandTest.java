package ru.tinkoff.edu.java.bot.test.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.client.scrapper.IScrapperClient;
import ru.tinkoff.edu.java.bot.component.textprovider.StartCommandTextProvider;
import ru.tinkoff.edu.java.bot.model.service.TestMessageTemplates;
import ru.tinkoff.edu.java.bot.model.telegram.TestChat;
import ru.tinkoff.edu.java.bot.model.telegram.TestMessage;
import ru.tinkoff.edu.java.bot.model.telegram.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.StartCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class StartCommandTest {

    private StartCommand startCommand;

    private IScrapperClient scrapperClient;

    private StartCommandTextProvider textProvider;

    @BeforeEach
    public void setup() {

        this.scrapperClient = Mockito.mock(IScrapperClient.class);
        this.textProvider = new StartCommandTextProvider(TestMessageTemplates.get());
        this.startCommand = new StartCommand(scrapperClient, textProvider);
    }

    @Test
    void testCommand() {
        // given

        // when
        var res = startCommand.command();

        // then
        assertThat(res, is(equalTo("/start")));
    }

    @Test
    void testDescription() {
        // given

        // when
        var res = startCommand.description();

        // then
        assertThat(res, is(equalTo("зарегистрировать пользователя")));
    }


    @Test
    void testSupportsValidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/start"));

        // when
        var res = startCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsValidCommand2() {
        // given
        var update = new TestUpdate(new TestMessage("/start aaa"));

        // when
        var res = startCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsInvalidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/hello"));

        // when
        var res = startCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsAnyMessage() {
        // given
        var update = new TestUpdate(new TestMessage("Any message"));

        // when
        var res = startCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsEmptyMessage() {
        // given
        var update = new TestUpdate(new TestMessage(""));

        // when
        var res = startCommand.supports(update);

        // then
        assertThat(res, is(false));
    }


    @Test
    void testHandleCommandWithValidListResponse() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/start", new TestChat(chatId)));
        doNothing().when(scrapperClient).registerChatById(anyLong(), anyString());


        // when
        var res = startCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, equalTo(textProvider.getStartMessage()));

        assertThat(parameters.get("disable_web_page_preview"), is(true));

    }

}
