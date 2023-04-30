package ru.tinkoff.edu.java.scrapper.reposotory.jdbc;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.StreamUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData.*;

@SpringBootTest
@ActiveProfiles("test")
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<LinkOutput> rowMapper;

    private final String selectLinkSql = """
                select * from link
                """;

    private final String insertLinkSql = """
                insert into link(url, state, last_scanned_at, created_at)
                values (?, ?::json, ?, ?)
                returning link_id
                """;

    @Transactional
    @Rollback
    @Test
    void testAddValidChat() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var request = new AddLinkInput(url, state, lastScannedAt, createdAt);


        // when
        var linkId = jdbcLinkRepository.save(request);


        // then
        assertThat(linkId, is(notNullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(linkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getState(), is(equalTo(state)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidRepeatedChat() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var request = new AddLinkInput(url, state, lastScannedAt, createdAt);

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, state.asJson(), lastScannedAt, createdAt);


        // when
        var linkId = jdbcLinkRepository.save(request);


        // then
        assertThat(linkId, is(nullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getState(), is(equalTo(state)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidChat() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, state.asJson(), lastScannedAt, createdAt);


        // when
        var response = jdbcLinkRepository.remove(savedLinkId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getState(), is(equalTo(state)));
        assertThat(response.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteUnknownChat() {
        // given
        var linkId = randomId();


        // when
        var response = jdbcLinkRepository.remove(linkId);


        // then
        assertThat(response, is(nullValue()));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll() {
        // given
        var list = stabValidResponse();
        var addRequestsList = list.stream().map(request -> {
                    var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            request.url(), request.state().asJson(), request.lastScannedAt(), request.createdAt());

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setState(request.state());
                    output.setLastScannedAt(request.lastScannedAt());
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectLinkSql, rowMapper);


        // when
        var response = jdbcLinkRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), addRequestsList.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getUrl(), is(equalTo(addTgChat.getUrl())));
                    assertThat(tgChat.getState(), is(equalTo(addTgChat.getState())));
                    assertThat(tgChat.getLastScannedAt().toEpochSecond(), is(equalTo(addTgChat.getLastScannedAt().toEpochSecond())));
                    assertThat(tgChat.getCreatedAt().toEpochSecond(), is(equalTo(addTgChat.getCreatedAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectLinkSql, rowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAllEmptyList() {
        // given
        var list = stabEmptyResponse();
        var addRequestsList = list.stream().map(request -> {
                    var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            request.url(), request.state().asJson(), request.lastScannedAt(), request.createdAt());

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setState(request.state());
                    output.setLastScannedAt(request.lastScannedAt());
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectLinkSql, rowMapper);


        // when
        var response = jdbcLinkRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), addRequestsList.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getUrl(), is(equalTo(addTgChat.getUrl())));
                    assertThat(tgChat.getState(), is(equalTo(addTgChat.getState())));
                    assertThat(tgChat.getLastScannedAt().toEpochSecond(), is(equalTo(addTgChat.getLastScannedAt().toEpochSecond())));
                    assertThat(tgChat.getCreatedAt().toEpochSecond(), is(equalTo(addTgChat.getCreatedAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectLinkSql, rowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindKnownChat() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url, state.asJson(), lastScannedAt, createdAt);


        // when
        var response = jdbcLinkRepository.findById(savedLinkId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getState(), is(equalTo(state)));
        assertThat(response.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getState(), is(equalTo(state)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUnknownChat() {
        // given
        var linkId = randomId();


        // when
        var response = jdbcLinkRepository.findById(linkId);


        // then
        assertThat(response, is(nullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testFindKnownChatByUrl() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url, state.asJson(), lastScannedAt, createdAt);


        // when
        var response = jdbcLinkRepository.findByUrl(url);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getState(), is(equalTo(state)));
        assertThat(response.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getState(), is(equalTo(state)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUnknownChatByUrl() {
        // given
        var url = "http://someurl.com";


        // when
        var response = jdbcLinkRepository.findByUrl(url);


        // then
        assertThat(response, is(nullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllByLastScannedAtIsBefore1() {
        // given
        var list = stabEmptyResponse();
        list.forEach(request -> jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                request.state().asJson(), request.url(), request.lastScannedAt(), request.createdAt()));


        // when
         var response = jdbcLinkRepository.findAllByLastScannedAtIsBefore(startDate);


        // then
        assertThat(response, is(notNullValue()));


        assertThat(response, hasSize(0));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllByLastScannedAtIsBefore2() {
        // given
        var list = stabEmptyResponse();
        list.forEach(request -> jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                request.url(), request.state().asJson(), request.lastScannedAt(), request.createdAt()));


        // when
        var response = jdbcLinkRepository.findAllByLastScannedAtIsBefore(endDate);


        // then
        assertThat(response, is(notNullValue()));


        assertThat(response, hasSize(list.size()));
    }


    @Transactional
    @Rollback
    @Test
    void testUpdateLastScannedAt() {
        // given
        var url = "http://someurl.com";
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, state.asJson(), lastScannedAt, createdAt);

        var updatedTime = randomDate();


        // when
        jdbcLinkRepository.updateLastScannedAt(savedLinkId, state, updatedTime);


        // then
        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getState(), is(equalTo(state)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(updatedTime.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }
}