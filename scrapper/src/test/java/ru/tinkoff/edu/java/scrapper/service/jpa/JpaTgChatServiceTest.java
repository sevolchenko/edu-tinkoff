package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestTgChatData.randomId;

@SpringBootTest
@ActiveProfiles({"test", "test-jpa"})
public class JpaTgChatServiceTest extends IntegrationEnvironment {

    @Autowired
    private JpaTgChatService tgChatService;

    @Autowired
    private JpaTgChatRepository tgChatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<TgChatOutput> rowMapper;

    private final String selectTgChatSql = """
            select * from tg_chat
            """;
    private final String insertTgChatSql = """
            insert into tg_chat(tg_chat_id, username, registered_at)
            values (?, ?, ?)
            """;

    @Transactional
    @Rollback
    @Test
    void testAddRegisterValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";

        // when
        TgChatOutput res = tgChatService.register(tgChatId, username);
        tgChatRepository.flush();

        // then
        assertThat(res, is(notNullValue()));
        assertThat(res.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(res.getUsername(), is(equalTo(username)));
        assertThat(res.getRegisteredAt().toEpochSecond(), is(lessThanOrEqualTo(OffsetDateTime.now().toEpochSecond())));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(res.getTgChatId())));
        assertThat(row.getUsername(), is(equalTo(res.getUsername())));
        assertThat(row.getRegisteredAt().toEpochSecond(), is(equalTo(res.getRegisteredAt().toEpochSecond())));

    }


    @Transactional
    @Rollback
    @Test
    void testRegisterDuplicatedChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        // when
        var ex = assertThrows(AlreadyRegisteredChatException.class, () ->
                tgChatService.register(tgChatId, username));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("Chat already registered: %d", tgChatId))));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
        assertThat(row.getRegisteredAt().toEpochSecond(), is(equalTo(registeredAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);


        // when
        var response = tgChatService.unregister(tgChatId);
        tgChatRepository.flush();


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getUsername(), is(equalTo(username)));
        assertThat(response.getRegisteredAt().toEpochSecond(), is(equalTo(registeredAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testAddDeleteUnknownChat() {
        // given
        var tgChatId = randomId();


        // when
        var ex = assertThrows(NoSuchChatException.class, () -> tgChatService.unregister(tgChatId));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("There is no chat with id %d", tgChatId))));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }

}
