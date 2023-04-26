package ru.tinkoff.edu.java.scrapper.dbtest.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.dbtest.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestTgChatData.*;

@SpringBootTest
@ActiveProfiles({"test", "test-jdbc"})
public class JdbcTgChatRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcTgChatRepository jdbcTgChatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String selectTgChatSql = """
                select * from tg_chat
                """;

    private final String insertTgChatSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, now())
                """;

    @Transactional
    @Rollback
    @Test
    void testAddValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var request = new RegisterTgChatInput(tgChatId, username);


        // when
        var response = jdbcTgChatRepository.add(request);


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response, is(equalTo(tgChatId)));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
        assertThat(row.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidRepeatedChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var request = new RegisterTgChatInput(tgChatId, username);

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);


        // when // add same user second time
        var ex = assertThrows(AlreadyRegisteredChatException.class, () -> jdbcTgChatRepository.add(request));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("Chat with id %d already registered", tgChatId))));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
        assertThat(row.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);


        // when
        var response = jdbcTgChatRepository.remove(tgChatId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getUsername(), is(equalTo(username)));
        assertThat(response.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

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
        var ex = assertThrows(NoSuchChatException.class, () -> jdbcTgChatRepository.remove(tgChatId));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("There is no chat with id %d", tgChatId))));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll1() {
        // given
        var list = stabValidResponse();
        list.forEach(request -> jdbcTemplate.update(insertTgChatSql, request.tgChatId(), request.username()));

        var rs1 = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));


        // when
        var response = jdbcTgChatRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(list.size()));

        int i = 0;
        for (TgChatOutput tgChatOutput : response) {
            var listResponse = list.get(i);

            assertThat(tgChatOutput, is(notNullValue()));
            assertThat(tgChatOutput.getTgChatId(), is(equalTo(listResponse.tgChatId())));
            assertThat(tgChatOutput.getUsername(), is(equalTo(listResponse.username())));
            assertThat(tgChatOutput.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

            i++;
        }

        var rs2 = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll2() {
        // given
        var list = stabEmptyResponse();
        list.forEach(request -> jdbcTemplate.update(insertTgChatSql, request.tgChatId(), request.username()));

        var rs1 = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));


        // when
        var response = jdbcTgChatRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(list.size())); // 0

        int i = 0;
        for (TgChatOutput tgChatOutput : response) {
            var listResponse = list.get(i);

            assertThat(tgChatOutput, is(notNullValue()));
            assertThat(tgChatOutput.getTgChatId(), is(equalTo(listResponse.tgChatId())));
            assertThat(tgChatOutput.getUsername(), is(equalTo(listResponse.username())));
            assertThat(tgChatOutput.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

            i++;
        }

        var rs2 = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindKnownChat() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);


        // when
        var response = jdbcTgChatRepository.findByTgChatId(tgChatId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getUsername(), is(equalTo(username)));
        assertThat(response.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
        assertThat(row.getRegisteredAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUnknownChat() {
        // given
        var tgChatId = randomId();


        // when
        var response = jdbcTgChatRepository.findByTgChatId(tgChatId);


        // then
        assertThat(response, is(nullValue()));

        var rs = jdbcTemplate.query(selectTgChatSql, new BeanPropertyRowMapper<>(TgChatOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }
}
