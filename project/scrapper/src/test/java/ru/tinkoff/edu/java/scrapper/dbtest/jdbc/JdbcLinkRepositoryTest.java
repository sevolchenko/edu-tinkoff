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
import ru.tinkoff.edu.java.scrapper.exception.AlreadyAddedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcLinkRepository;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestLinkData.stabEmptyResponse;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestLinkData.stabValidResponse;
import static ru.tinkoff.edu.java.scrapper.dbtest.data.TestTgChatData.randomId;

@SpringBootTest
@ActiveProfiles({"test", "test-jdbc"})
public class JdbcLinkRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String selectLinkSql = """
                select s.link_id, url, last_scanned_at, s.created_at from subscription s, link l
                where s.link_id = l.link_id
                """;
    private final String insertTgChatSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, now())
                """;

    private final String insertLinkSql = """
                insert into link(url, last_scanned_at, created_at)
                values (?, now(), now())
                returning link_id
                """;

    private final String insertLinkWithScannedTimeSql = """
                insert into link(url, last_scanned_at, created_at)
                values (?, ?, now())
                returning link_id
                """;

    private final String insertSubSql = """
                insert into subscription(tg_chat_id, link_id, created_at)
                values (?, ?, now())
                """;

    @Transactional
    @Rollback
    @Test
    void testAddValidLink() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        // when
        var response = jdbcLinkRepository.add(request);


        // then
        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(response)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getCreatedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
        assertThat(row.getLastScannedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

    }


    @Transactional
    @Rollback
    @Test
    void testAddRepeatedValidLink() {
        // given
        var tgChatId = randomId();
        var username = "username";

        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url);
        jdbcTemplate.update(insertSubSql, tgChatId, linkId);


        // when // add same link second time
        var ex = assertThrows(AlreadyAddedLinkException.class, () -> jdbcLinkRepository.add(request));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("Link already added to id %d: %s", tgChatId, url))));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));
    }


    @Rollback
    @Test
    void testAddNoTgChatLink() {
        // given
        var tgChatId = randomId();
        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        // when
        var ex = assertThrows(NoSuchChatException.class, () -> jdbcLinkRepository.add(request));


        // then
        assertThat(ex.getMessage(), is(equalTo(String.format("There is no chat with id %d", tgChatId))));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidLink() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url);
        jdbcTemplate.update(insertSubSql, tgChatId, linkId);


        // when
        var response = jdbcLinkRepository.remove(request);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(linkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getLastScannedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
        assertThat(response.getCreatedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteUnknownLink() {
        // given
        var tgChatId = randomId();
        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);


        // when
        var ex = assertThrows(NoSuchLinkException.class, () -> jdbcLinkRepository.remove(request));


        // then
        assertThat(ex, is(notNullValue()));
        assertThat(ex.getMessage(), is(equalTo(String.format("Link has not added to id %d yet: %s", tgChatId, url))));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }




    @Transactional
    @Rollback
    @Test
    void testFindUncheckedLinks() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var url = "https://localhost:8081";

        Duration linkCheckDelay = Duration.ofHours(1);

        OffsetDateTime lastScannedAt = OffsetDateTime.now().minus(2, ChronoUnit.HOURS);

        var linkId = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url, lastScannedAt);
        jdbcTemplate.update(insertSubSql, tgChatId, linkId);


        // when
        var response = jdbcLinkRepository.findUncheckedLinks(linkCheckDelay);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(1));

        var row = response.iterator().next();

        assertThat(row, is(notNullValue()));
        assertThat(row.linkId(), is(equalTo(linkId)));
        assertThat(row.url(), is(equalTo(url)));

        var tgChatIds = row.tgChatIds();

        assertThat(tgChatIds, hasSize(1));
        assertThat(tgChatIds.get(0), is(equalTo(tgChatId)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUncheckedBigDelayLinks() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var url = "https://localhost:8081";

        Duration linkCheckDelay = Duration.ofHours(1);

        OffsetDateTime lastScannedAt = OffsetDateTime.now();

        var linkId = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url, lastScannedAt);
        jdbcTemplate.update(insertSubSql, tgChatId, linkId);


        // when
        var response = jdbcLinkRepository.findUncheckedLinks(linkCheckDelay);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUncheckedLinksStacks() {
        // given
        var tgChatId1 = randomId();
        var username1 = "username1";
        var tgChatId2 = randomId();
        var username2 = "username2";

        jdbcTemplate.update(insertTgChatSql, tgChatId1, username1);
        jdbcTemplate.update(insertTgChatSql, tgChatId2, username2);

        var url = "https://localhost:8081";

        Duration linkCheckDelay = Duration.ofHours(1);

        OffsetDateTime lastScannedAt = OffsetDateTime.now().minus(2, ChronoUnit.HOURS);

        var linkId = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url, lastScannedAt);
        jdbcTemplate.update(insertSubSql, tgChatId1, linkId);
        jdbcTemplate.update(insertSubSql, tgChatId2, linkId);


        // when
        var response = jdbcLinkRepository.findUncheckedLinks(linkCheckDelay);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(1));

        var row = response.iterator().next();

        assertThat(row, is(notNullValue()));
        assertThat(row.linkId(), is(equalTo(linkId)));
        assertThat(row.url(), is(equalTo(url)));

        var tgChatIds = row.tgChatIds();

        assertThat(tgChatIds, hasSize(2));
        assertThat(tgChatIds.get(0), is(equalTo(tgChatId1)));
        assertThat(tgChatIds.get(1), is(equalTo(tgChatId2)));

    }


    @Transactional
    @Rollback
    @Test
    void testFindUncheckedLinksStacksSeveralLinks() {
        // given
        var tgChatId1 = randomId();
        var username1 = "username1";
        var tgChatId2 = randomId();
        var username2 = "username2";

        jdbcTemplate.update(insertTgChatSql, tgChatId1, username1);
        jdbcTemplate.update(insertTgChatSql, tgChatId2, username2);

        var url1 = "https://localhost:8081";
        var url2 = "https://localhost:8082";

        Duration linkCheckDelay = Duration.ofHours(1);

        OffsetDateTime lastScannedAt1 = OffsetDateTime.now().minus(2, ChronoUnit.HOURS);
        OffsetDateTime lastScannedAt2 = OffsetDateTime.now();

        var linkId1 = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url1, lastScannedAt1);
        var linkId2 = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url2, lastScannedAt2);
        jdbcTemplate.update(insertSubSql, tgChatId1, linkId1);
        jdbcTemplate.update(insertSubSql, tgChatId1, linkId2);
        jdbcTemplate.update(insertSubSql, tgChatId2, linkId1);


        // when
        var response = jdbcLinkRepository.findUncheckedLinks(linkCheckDelay);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(1));

        var row = response.iterator().next();

        assertThat(row, is(notNullValue()));
        assertThat(row.linkId(), is(equalTo(linkId1)));
        assertThat(row.url(), is(equalTo(url1)));

        var tgChatIds = row.tgChatIds();

        assertThat(tgChatIds, hasSize(2));
        assertThat(tgChatIds.get(0), is(equalTo(tgChatId1)));
        assertThat(tgChatIds.get(1), is(equalTo(tgChatId2)));

    }

    @Transactional
    @Rollback
    @Test
    void testFindUncheckedLinksStacksSeveralLinks2() {
        // given
        var tgChatId1 = randomId();
        var username1 = "username1";
        var tgChatId2 = randomId();
        var username2 = "username2";

        jdbcTemplate.update(insertTgChatSql, tgChatId1, username1);
        jdbcTemplate.update(insertTgChatSql, tgChatId2, username2);

        var url1 = "https://localhost:8081";
        var url2 = "https://localhost:8082";

        Duration linkCheckDelay = Duration.ofHours(1);

        OffsetDateTime lastScannedAt1 = OffsetDateTime.now().minus(2, ChronoUnit.HOURS);
        OffsetDateTime lastScannedAt2 = OffsetDateTime.now().minus(3, ChronoUnit.HOURS);;

        var linkId1 = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url1, lastScannedAt1);
        var linkId2 = jdbcTemplate.queryForObject(insertLinkWithScannedTimeSql, Long.class, url2, lastScannedAt2);
        jdbcTemplate.update(insertSubSql, tgChatId1, linkId1);
        jdbcTemplate.update(insertSubSql, tgChatId1, linkId2);
        jdbcTemplate.update(insertSubSql, tgChatId2, linkId1);


        // when
        var response = jdbcLinkRepository.findUncheckedLinks(linkCheckDelay);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(2));

        var it = response.iterator();
        var row1 = it.next();

        assertThat(row1, is(notNullValue()));
        assertThat(row1.linkId(), is(equalTo(linkId1)));
        assertThat(row1.url(), is(equalTo(url1)));

        var tgChatIds1 = row1.tgChatIds();

        assertThat(tgChatIds1, hasSize(2));
        assertThat(tgChatIds1.get(0), is(equalTo(tgChatId1)));
        assertThat(tgChatIds1.get(1), is(equalTo(tgChatId2)));

        var row2 = it.next();

        assertThat(row2, is(notNullValue()));
        assertThat(row2.linkId(), is(equalTo(linkId2)));
        assertThat(row2.url(), is(equalTo(url2)));

        var tgChatIds2 = row2.tgChatIds();

        assertThat(tgChatIds2, hasSize(1));
        assertThat(tgChatIds2.get(0), is(equalTo(tgChatId1)));

    }


    @Transactional
    @Rollback
    @Test
    void testFindAll1() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var list = stabValidResponse();
        list.forEach(linkResponse -> {
            var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, linkResponse.getUrl());
            linkResponse.setLinkId(linkId);
            jdbcTemplate.update(insertSubSql, tgChatId, linkId);
        });

        var rs1 = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        // when
        var response = jdbcLinkRepository.findAll(tgChatId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(list.size()));

        int i = 0;
        for (LinkOutput linkOutput : response) {
            LinkOutput listResponse = list.get(i);

            assertThat(linkOutput, is(notNullValue()));
            assertThat(linkOutput.getLinkId(), is(equalTo(listResponse.getLinkId())));
            assertThat(linkOutput.getUrl(), is(equalTo(listResponse.getUrl())));

            i++;
        }

        var rs2 = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll2() {
        // given
        var tgChatId = randomId();
        var username = "username";

        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var list = stabEmptyResponse();
        list.forEach(linkResponse -> {
            var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, linkResponse.getUrl());
            jdbcTemplate.update(insertSubSql, tgChatId, linkId);
        });

        var rs1 = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        // when
        var response = jdbcLinkRepository.findAll(tgChatId);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response, hasSize(list.size()));

        int i = 0;
        for (LinkOutput linkOutput : response) {
            LinkOutput listResponse = list.get(i);

            assertThat(linkOutput, is(notNullValue()));
            assertThat(linkOutput.getLinkId(), is(equalTo(listResponse.getLinkId())));
            assertThat(linkOutput.getUrl(), is(equalTo(listResponse.getUrl())));

            i++;
        }

        var rs2 = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));
        assertThat(rs1, is(equalTo(rs2)));
    }


    @Transactional
    @Rollback
    @Test
    void testFindKnownLink() {
        // given
        var tgChatId = randomId();
        var username = "username";
        jdbcTemplate.update(insertTgChatSql, tgChatId, username);

        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url);
        jdbcTemplate.update(insertSubSql, tgChatId, linkId);


        // when
        var response = jdbcLinkRepository.findById(request);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getLinkId(), is(equalTo(linkId)));
        assertThat(response.getUrl(), is(equalTo(url)));
        assertThat(response.getCreatedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
        assertThat(response.getLastScannedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getLinkId(), is(equalTo(linkId)));
        assertThat(row.getUrl(), is(equalTo(url)));
        assertThat(row.getCreatedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
        assertThat(row.getLastScannedAt(), is(lessThanOrEqualTo(OffsetDateTime.now())));
    }

    @Transactional
    @Rollback
    @Test
    void testFindUnknownLink() {
        // given
        var tgChatId = randomId();
        var url = "https://localhost:8081";
        var request = new SubscriptionInput(tgChatId, url);

        // when
        var response = jdbcLinkRepository.findById(request);


        // then
        assertThat(response, is(nullValue()));

        var rs = jdbcTemplate.query(selectLinkSql, new BeanPropertyRowMapper<>(LinkOutput.class));

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }

}
