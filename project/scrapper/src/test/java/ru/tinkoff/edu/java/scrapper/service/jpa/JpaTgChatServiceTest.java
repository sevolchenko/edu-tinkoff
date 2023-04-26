package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import ru.tinkoff.edu.java.scrapper.dbtest.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestTgChatData.randomId;

@SpringBootTest
@ActiveProfiles({"test", "test-jpa"})
public class JpaTgChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private JpaTgChatService tgChatService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String selectTgChatSql = """
                select * from tg_chat
                """;
    private final String insertTgChatSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, now())
                """;


    @Test
    @Rollback
    void testAddRegisterValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";

        // when
        TgChatOutput res = tgChatService.register(new RegisterTgChatInput(tgChatId, username));

        // then
        assertThat(res, is(notNullValue()));
        assertThat(res.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(res.getUsername(), is(equalTo(username)));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(res.getTgChatId())));
        assertThat(row.getUsername(), is(equalTo(res.getUsername())));
        assertThat(row.getRegisteredAt().toEpochSecond(), is(equalTo(res.getRegisteredAt().toEpochSecond())));

    }


    @Test
    @Rollback
    void testRegisterDuplicatedChat() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        // when // add same user second time
        var ex = assertThrows(AlreadyRegisteredChatException.class, () ->
                tgChatService.register(new RegisterTgChatInput(tgChatId, username)));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("Chat with id %d already registered", tgChatId))));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
    }

}
