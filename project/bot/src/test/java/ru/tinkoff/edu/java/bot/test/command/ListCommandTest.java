package ru.tinkoff.edu.java.bot.test.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.tinkoff.edu.java.bot.client.IScrapperClient;
import ru.tinkoff.edu.java.bot.model.TestChat;
import ru.tinkoff.edu.java.bot.model.TestMessage;
import ru.tinkoff.edu.java.bot.model.TestUpdate;
import ru.tinkoff.edu.java.bot.service.command.ListCommand;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static ru.tinkoff.edu.java.bot.test.data.TestListLinkResponseData.*;

public class ListCommandTest {

    private ListCommand listCommand;

    private IScrapperClient scrapperClient;

    @BeforeEach
    public void setup() {

        this.scrapperClient = Mockito.mock(IScrapperClient.class);
        this.listCommand = new ListCommand(scrapperClient);

    }

     @Test
     void testCommand() {
         // given

         // when
         var res = listCommand.command();

         // then
         assertThat(res, is(equalTo("/list")));
     }

    @Test
    void testDescription() {
        // given

        // when
        var res = listCommand.description();

        // then
        assertThat(res, is(equalTo("показать список отслеживаемых ссылок")));
    }


    @Test
    void testSupportsValidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/list"));

        // when
        var res = listCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsValidCommand2() {
        // doNothing().when(scrapperClient).registerChatById(anyLong());
        // given
        var update = new TestUpdate(new TestMessage("/list aaa"));

        // when
        var res = listCommand.supports(update);

        // then
        assertThat(res, is(true));
    }

    @Test
    void testSupportsInvalidCommand() {
        // given
        var update = new TestUpdate(new TestMessage("/hello"));

        // when
        var res = listCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsAnyMessage() {
        // given
        var update = new TestUpdate(new TestMessage("Any message"));

        // when
        var res = listCommand.supports(update);

        // then
        assertThat(res, is(false));
    }

    @Test
    void testSupportsEmptyMessage() {
        // given
        var update = new TestUpdate(new TestMessage(""));

        // when
        var res = listCommand.supports(update);

        // then
        assertThat(res, is(false));
    }


    @Test
    void testHandleCommandWithValidListResponse() {

        // given
        Long chatId = randomId();
        var list = stabValidResponse(chatId);

        var update = new TestUpdate(new TestMessage("/list", new TestChat(chatId)));
        when(scrapperClient.getLinks(chatId))
                .thenReturn(list);


        // when
        var res = listCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, startsWith("Вот список ссылок, которые ты отслеживаешь"));
        assertThat(text, containsString(String.format("(всего %d)", list.size())));
        assertThat(text, stringContainsInOrder(list.links().stream()
                .map(linkResponse -> "\n- " + linkResponse.link().toString())
                .toList()));

        assertThat(parameters.get("disable_web_page_preview"), is(true));

    }

    @Test
    void testHandleCommandWithBigValidListResponse() {

        // given
        Long chatId = randomId();
        var list = stabValidBigResponse(chatId);

        var update = new TestUpdate(new TestMessage("/list", new TestChat(chatId)));
        when(scrapperClient.getLinks(chatId))
                .thenReturn(list);


        // when
        var res = listCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, startsWith("Вот список ссылок, которые ты отслеживаешь"));
        assertThat(text, containsString(String.format("(всего %d)", list.size())));
        assertThat(text, stringContainsInOrder(list.links().stream()
                .map(linkResponse -> "\n- " + linkResponse.link().toString())
                .toList()));

        assertThat(parameters.get("disable_web_page_preview"), is(true));

    }

    @Test
    void testHandleCommandWithEmptyListResponse() {

        // given
        Long chatId = randomId();
        var list = stabEmptyResponse();

        var update = new TestUpdate(new TestMessage("/list", new TestChat(chatId)));
        when(scrapperClient.getLinks(chatId))
                .thenReturn(list);


        // when
        var res = listCommand.handle(update);


        // then
        assertThat(res, is(notNullValue()));

        var parameters = res.getParameters();
        assertThat(parameters.get("chat_id"), is(equalTo(chatId)));

        String text = (String) parameters.get("text");
        assertThat(text, equalTo("Ты еще не отслеживаешь ни одной ссылки"));

    }

}
