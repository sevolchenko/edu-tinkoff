package ru.tinkoff.edu.java.scrapper.reposotory.jooq;

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
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqLinkRepository;

import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData.*;

@SpringBootTest
@ActiveProfiles("test")
public class JooqLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JooqLinkRepository jooqLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<LinkOutput> rowMapper = new BeanPropertyRowMapper<>(LinkOutput.class);

    private final String selectLinkSql = """
                select * from link
                """;

    private final String insertLinkSql = """
                insert into link(url, last_scanned_at, created_at)
                values (?, ?, ?)
                returning link_id
                """;

    @Transactional
    @Rollback
    @Test
    void testAddValidChat() {
        // given
        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var request = new AddLinkInput(url, lastScannedAt, createdAt);


        // when
        var linkId = jooqLinkRepository.save(request);


        // then
        assertThat(linkId, is(notNullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(linkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidRepeatedChat() {
        // given
        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var request = new AddLinkInput(url, lastScannedAt, createdAt);

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);


        // when
        var linkId = jooqLinkRepository.save(request);


        // then
        assertThat(linkId, is(nullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidChat() {
        // given
        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);


        // when
        var response = jooqLinkRepository.remove(savedLinkId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
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
        var response = jooqLinkRepository.remove(linkId);


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
                            request.url(), request.lastScannedAt(), request.createdAt());

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setLastScannedAt(request.lastScannedAt());
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectLinkSql, rowMapper);


        // when
        var response = jooqLinkRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), addRequestsList.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getUrl(), is(equalTo(addTgChat.getUrl())));
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
                            request.url(), request.lastScannedAt(), request.createdAt());

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setLastScannedAt(request.lastScannedAt());
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectLinkSql, rowMapper);


        // when
        var response = jooqLinkRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));


        StreamUtils.zip(response.stream(), addRequestsList.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getUrl(), is(equalTo(addTgChat.getUrl())));
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
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);


        // when
        var response = jooqLinkRepository.findById(savedLinkId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
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
        var response = jooqLinkRepository.findById(linkId);


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
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);


        // when
        var response = jooqLinkRepository.findByUrl(url);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getLastScannedAt().toEpochSecond(), is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
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
        var response = jooqLinkRepository.findByUrl(url);


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
                request.url(), request.lastScannedAt(), request.createdAt()));


        // when
        var response = jooqLinkRepository.findAllByLastScannedAtIsBefore(startDate);


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
                request.url(), request.lastScannedAt(), request.createdAt()));


        // when
        var response = jooqLinkRepository.findAllByLastScannedAtIsBefore(endDate);


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
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);

        var updatedTime = randomDate();


        // when
        jooqLinkRepository.updateLastScannedAt(savedLinkId, updatedTime);


        // then
        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(updatedTime.toEpochSecond())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(createdAt.toEpochSecond())));
    }
}
