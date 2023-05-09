package ru.tinkoff.edu.java.bot.test.command;

import com.pengrad.telegrambot.model.BotCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.tinkoff.edu.java.bot.component.textprovider.HelpCommandTextProvider;
import ru.tinkoff.edu.java.bot.model.service.TestMessageTemplates;
import ru.tinkoff.edu.java.bot.model.telegram.TestChat;
import ru.tinkoff.edu.java.bot.model.telegram.TestMessage;
import ru.tinkoff.edu.java.bot.model.telegram.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.HelpCommand;
import ru.tinkoff.edu.java.bot.test.data.TestBotCommandsList;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.randomId;

public class HelpCommandTest {

    private HelpCommand helpCommand;

    private HelpCommandTextProvider textProvider;
    private final List<BotCommand> botCommands = TestBotCommandsList.BOT_COMMANDS;

    @BeforeEach
    public void setup() {

        this.textProvider = new HelpCommandTextProvider(TestMessageTemplates.get());
        this.helpCommand = new HelpCommand(textProvider);
        helpCommand.setBotCommands(botCommands);

    }

    @Test
    void testCommand() {
        // given

        // when
        var res = helpCommand.command();

        // then
        assertThat(res, is(equalTo("/help")));
    }

    @Test
    void testDescription() {
        // given

        // when
        var res = helpCommand.description();

        // then
        assertThat(res, is(equalTo("вывести окно с командами")));
    }


    @Test
    void testSupportsValidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/help"));

        // when
        var res = helpCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsValidCommand2() {
        // given
        var update = new TestUpdate(new TestMessage("/help aaa"));

        // when
        var res = helpCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsInvalidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/hello"));

        // when
        var res = helpCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsAnyMessage() {
        // given
        var update = new TestUpdate(new TestMessage("Any message"));

        // when
        var res = helpCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsEmptyMessage() {
        // given
        var update = new TestUpdate(new TestMessage(""));

        // when
        var res = helpCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testHandleCommandWithValidCommandsList() {

        // given
        Long chatId = randomId();

        var update = new TestUpdate(new TestMessage("/help", new TestChat(chatId)));


        // when
        var res = helpCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, equalTo(textProvider.getHelpText(botCommands)));

    }


}
