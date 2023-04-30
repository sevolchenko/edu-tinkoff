package ru.tinkoff.edu.java.scrapper.reposotory.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.StreamUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcTgChatRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestTgChatData.*;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcTgChatRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcTgChatRepository jdbcTgChatRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<TgChatOutput> rowMapper = new BeanPropertyRowMapper<>(TgChatOutput.class);

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
    void testAddValidChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        var request = new AddTgChatInput(tgChatId, username, registeredAt);


        // when
        var tgChat = jdbcTgChatRepository.save(request);


        // then
        assertThat(tgChat, is(notNullValue()));

        assertThat(tgChat, is(equalTo(tgChatId)));

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
    void testAddValidRepeatedChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        var request = new AddTgChatInput(tgChatId, username, registeredAt);

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);


        // when
        var tgChat = jdbcTgChatRepository.save(request);


        // then
        assertThat(tgChat, is(nullValue()));

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
        var response = jdbcTgChatRepository.remove(tgChatId);


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
    void testDeleteUnknownChat() {
        // given
        var tgChatId = randomId();


        // when
        var response = jdbcTgChatRepository.remove(tgChatId);


        // then
        assertThat(response, is(nullValue()));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll() {
        // given
        var list = stabValidResponse();
        list.forEach(request -> jdbcTemplate.update(insertTgChatSql, request.tgChatId(), request.username(), request.registeredAt()));

        var rs1 = jdbcTemplate.query(selectTgChatSql, rowMapper);


        // when
        var response = jdbcTgChatRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), list.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getTgChatId(), is(equalTo(addTgChat.tgChatId())));
                    assertThat(tgChat.getUsername(), is(equalTo(addTgChat.username())));
                    assertThat(tgChat.getRegisteredAt().toEpochSecond(), is(equalTo(addTgChat.registeredAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectTgChatSql, rowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAllEmptyList() {
        // given
        var list = stabEmptyResponse();
        list.forEach(request -> jdbcTemplate.update(insertTgChatSql, request.tgChatId(), request.username()));

        var rs1 = jdbcTemplate.query(selectTgChatSql, rowMapper);


        // when
        var response = jdbcTgChatRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), list.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getTgChatId(), is(equalTo(addTgChat.tgChatId())));
                    assertThat(tgChat.getUsername(), is(equalTo(addTgChat.username())));
                    assertThat(tgChat.getRegisteredAt().toEpochSecond(), is(equalTo(addTgChat.registeredAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectTgChatSql, rowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindKnownChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);


        // when
        var response = jdbcTgChatRepository.findById(tgChatId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getUsername(), is(equalTo(username)));
        assertThat(response.getRegisteredAt().toEpochSecond(), is(equalTo(registeredAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getUsername(), is(equalTo(username)));
        assertThat(response.getRegisteredAt().toEpochSecond(), is(equalTo(registeredAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUnknownChat() {
        // given
        var tgChatId = randomId();


        // when
        var response = jdbcTgChatRepository.findById(tgChatId);


        // then
        assertThat(response, is(nullValue()));

        var rs = jdbcTemplate.query(selectTgChatSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }
}
