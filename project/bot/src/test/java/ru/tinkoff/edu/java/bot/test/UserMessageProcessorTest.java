package ru.tinkoff.edu.java.bot.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.model.telegram.TestChat;
import ru.tinkoff.edu.java.bot.model.telegram.TestMessage;
import ru.tinkoff.edu.java.bot.model.telegram.TestUpdate;
import ru.tinkoff.edu.java.bot.model.service.command.TestListCommand;
import ru.tinkoff.edu.java.bot.model.service.command.TestStartCommand;
import ru.tinkoff.edu.java.bot.model.service.command.TestTrackCommand;
import ru.tinkoff.edu.java.bot.model.service.command.TestUntrackCommand;
import ru.tinkoff.edu.java.bot.service.bot.IUserMessageProcessor;
import ru.tinkoff.edu.java.bot.service.command.*;
import ru.tinkoff.edu.java.bot.service.bot.impl.UserMessageProcessor;
import ru.tinkoff.edu.java.bot.util.TextProvider.BotTextProvider;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.verify;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class UserMessageProcessorTest {

    private HelpCommand helpCommand;
    private StartCommand startCommand;
    private TrackCommand trackCommand;
    private UntrackCommand untrackCommand;
    private ListCommand listCommand;

    private List<? extends Command> commands;

    private IUserMessageProcessor userMessageProcessor;

    @BeforeEach
    public void setup() {
        helpCommand = Mockito.spy(HelpCommand.class);
        startCommand = Mockito.spy(TestStartCommand.class);
        trackCommand = Mockito.spy(TestTrackCommand.class);
        untrackCommand = Mockito.spy(TestUntrackCommand.class);
        listCommand = Mockito.spy(TestListCommand.class);

        this.commands = List.of(
                helpCommand,
                startCommand,
                trackCommand,
                untrackCommand,
                listCommand
        );

        this.userMessageProcessor = new UserMessageProcessor(commands);

    }

    @Test
    void testCommands() {
        // given

        // when
        var res = userMessageProcessor.commands();

        // then
        assertThat(res, is(equalTo(commands)));
    }

    @Test
    void testProcessWithUnsupportedMessage() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("Unsupported cmd", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, equalTo(BotTextProvider.buildUnknownMessageText()));
    }

    @Test
    void testProcessWithUnsupportedCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/close", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, equalTo(BotTextProvider.buildUnknownMessageText()));
    }
    @Test
    void testProcessCallsStartCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/start", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        verify(startCommand).handle(update);
    }

    @Test
    void testProcessCallsHelpCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/help", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        verify(helpCommand).handle(update);
    }

    @Test
    void testProcessCallsTrackCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/track", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        verify(trackCommand).handle(update);
    }

    @Test
    void testProcessCallsUntrackCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/untrack", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        verify(untrackCommand).handle(update);
    }

    @Test
    void testProcessCallsListCommand() {
        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/list", new TestChat(chatId)));

        // when
        var res = userMessageProcessor.process(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        verify(listCommand).handle(update);
    }

}
