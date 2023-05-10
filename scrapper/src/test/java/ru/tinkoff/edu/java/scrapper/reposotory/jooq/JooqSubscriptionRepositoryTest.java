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
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddSubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionIdInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.repository.jooq.JooqSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData;
import ru.tinkoff.edu.java.scrapper.reposotory.data.TestTgChatData;

import java.time.OffsetDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestSubscriptionData.random;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestTgChatData.randomId;

@SpringBootTest
@ActiveProfiles("test")
public class JooqSubscriptionRepositoryTest extends IntegrationEnvironment {

    @Autowired
    private JooqSubscriptionRepository jooqSubscriptionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final RowMapper<SubscriptionOutput> subscriptionRowMapper = new BeanPropertyRowMapper<>(SubscriptionOutput.class);

    private final String selectSubscriptionSql = """
            select * from subscription
            """;

    private final String insertSubscriptionSql = """
                insert into subscription(tg_chat_id, link_id, created_at)
                values (?, ?, ?)
            """;

    private final String insertTgChatSql = """
            insert into tg_chat(tg_chat_id, username, registered_at)
            values (?, ?, ?)
            """;

    private final String insertLinkSql = """
            insert into link(url, last_scanned_at, created_at)
            values (?, ?, ?)
            returning link_id
            """;

    @Transactional
    @Rollback
    @Test
    void testAddValidSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();
        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);

        var subCreatedAt = randomDate();
        var request = new AddSubscriptionInput(tgChatId, savedLinkId, subCreatedAt);

        // when
        var sub = jooqSubscriptionRepository.save(request);


        // then
        assertThat(sub, is(notNullValue()));

        assertThat(sub.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(sub.getLinkId(), is(equalTo(savedLinkId)));

        var rs = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(subCreatedAt.toEpochSecond())));
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidRepeatedSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();
        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);

        var subCreatedAt = randomDate();

        jdbcTemplate.update(insertSubscriptionSql, tgChatId, savedLinkId, subCreatedAt);
        var request = new AddSubscriptionInput(tgChatId, savedLinkId, subCreatedAt);

        // when
        var sub = jooqSubscriptionRepository.save(request);


        // then
        assertThat(sub, is(nullValue()));

        var rs = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(subCreatedAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteValidSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();
        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);

        var subCreatedAt = randomDate();

        jdbcTemplate.update(insertSubscriptionSql, tgChatId, savedLinkId, subCreatedAt);
        var request = new SubscriptionIdInput(tgChatId, savedLinkId);


        // when
        var response = jooqSubscriptionRepository.remove(request);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(subCreatedAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testDeleteUnknownSub() {
        // given
        var tgChatId = randomId();
        var linkId = randomId();

        var request = new SubscriptionIdInput(tgChatId, linkId);

        // when
        var response = jooqSubscriptionRepository.remove(request);


        // then
        assertThat(response, is(nullValue()));
    }


    @Transactional
    @Rollback
    @Test
    void testFindAll() {
        // given
        var tgChats = TestTgChatData.stabValidResponse();
        var links = TestLinkData.stabValidResponse();

        List<SubscriptionOutput> outputs = StreamUtils.zip(tgChats.stream(), links.stream(), (tgChat, link) -> {
                    jdbcTemplate.update(insertTgChatSql,
                            tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
                    Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            link.url(), link.lastScannedAt(), link.createdAt());

                    var subCreatedAt = randomDate();
                    jdbcTemplate.update(insertSubscriptionSql, tgChat.tgChatId(), linkId, subCreatedAt);

                    var res = new SubscriptionOutput();
                    res.setTgChatId(tgChat.tgChatId());
                    res.setLinkId(linkId);
                    res.setCreatedAt(subCreatedAt);
                    return res;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);


        // when
        var response = jooqSubscriptionRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));

        StreamUtils.zip(outputs.stream(), response.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getTgChatId(), is(equalTo(addTgChat.getTgChatId())));
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getCreatedAt().toEpochSecond(), is(equalTo(addTgChat.getCreatedAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllEmpty() {
        // given
        var tgChats = TestTgChatData.stabEmptyResponse();
        var links = TestLinkData.stabEmptyResponse();

        List<SubscriptionOutput> outputs = StreamUtils.zip(tgChats.stream(), links.stream(), (tgChat, link) -> {
                    jdbcTemplate.update(insertTgChatSql,
                            tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
                    Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            link.url(), link.lastScannedAt(), link.createdAt());

                    var subCreatedAt = randomDate();
                    jdbcTemplate.update(insertSubscriptionSql, tgChat.tgChatId(), linkId, subCreatedAt);

                    var res = new SubscriptionOutput();
                    res.setTgChatId(tgChat.tgChatId());
                    res.setLinkId(linkId);
                    res.setCreatedAt(subCreatedAt);
                    return res;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);


        // when
        var response = jooqSubscriptionRepository.findAll();


        // then
        assertThat(response, is(notNullValue()));

        assertThat(outputs, is(equalTo(response)));

        var rs2 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllByLinkId1() {
        // given
        var tgChats = TestTgChatData.stabValidResponse();
        var links = TestLinkData.stabValidResponse();

        List<SubscriptionOutput> outputs = StreamUtils.zip(tgChats.stream(), links.stream(), (tgChat, link) -> {
                    jdbcTemplate.update(insertTgChatSql,
                            tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
                    Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            link.url(), link.lastScannedAt(), link.createdAt());

                    var subCreatedAt = randomDate();
                    jdbcTemplate.update(insertSubscriptionSql, tgChat.tgChatId(), linkId, subCreatedAt);

                    var res = new SubscriptionOutput();
                    res.setTgChatId(tgChat.tgChatId());
                    res.setLinkId(linkId);
                    res.setCreatedAt(subCreatedAt);
                    return res;
                })
                .toList();

        var rs1 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        int indexForCheck = random(outputs.size());


        // when
        var response = jooqSubscriptionRepository.findAllByLinkId(outputs.get(indexForCheck).getLinkId());


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response, hasSize(1));

        var row = response.get(0);
        var output = outputs.get(indexForCheck);

        assertThat(row.getTgChatId(), is(equalTo(output.getTgChatId())));
        assertThat(row.getLinkId(), is(equalTo(output.getLinkId())));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(output.getCreatedAt().toEpochSecond())));

        var rs2 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllByLinkId2() {
        // given
        var tgChats = TestTgChatData.stabValidResponse();

        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();

        var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);


        List<SubscriptionOutput> outputs = tgChats.stream().map((tgChat) -> {
            jdbcTemplate.update(insertTgChatSql,
                    tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());

            var subCreatedAt = randomDate();
            jdbcTemplate.update(insertSubscriptionSql, tgChat.tgChatId(), linkId, subCreatedAt);

            var res = new SubscriptionOutput();
            res.setTgChatId(tgChat.tgChatId());
            res.setLinkId(linkId);
            res.setCreatedAt(subCreatedAt);
            return res;
        }).toList();

        var rs1 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        // when
        var response = jooqSubscriptionRepository.findAllByLinkId(linkId);


        // then
        assertThat(response, is(notNullValue()));

        StreamUtils.zip(outputs.stream(), response.stream(),
                (tgChat, addTgChat) -> {
                    assertThat(tgChat.getTgChatId(), is(equalTo(addTgChat.getTgChatId())));
                    assertThat(tgChat.getLinkId(), is(equalTo(addTgChat.getLinkId())));
                    assertThat(tgChat.getCreatedAt().toEpochSecond(), is(equalTo(addTgChat.getCreatedAt().toEpochSecond())));
                    return null;
                });

        var rs2 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(rs1, is(equalTo(rs2)));
    }

    @Transactional
    @Rollback
    @Test
    void testFindAllByLinkId3() {
        // given
        var tgChats = TestTgChatData.stabValidResponse();
        var links = TestLinkData.stabValidResponse();

        StreamUtils.zip(tgChats.stream(), links.stream(), (tgChat, link) -> {
            jdbcTemplate.update(insertTgChatSql,
                    tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
            Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                    link.url(), link.lastScannedAt(), link.createdAt());

            var subCreatedAt = randomDate();
            jdbcTemplate.update(insertSubscriptionSql, tgChat.tgChatId(), linkId, subCreatedAt);

            var res = new SubscriptionOutput();
            res.setTgChatId(tgChat.tgChatId());
            res.setLinkId(linkId);
            res.setCreatedAt(subCreatedAt);
            return res;
        });

        var rs1 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        Long linkId = randomId();


        // when
        var response = jooqSubscriptionRepository.findAllByLinkId(linkId);


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response, hasSize(0));

        var rs2 = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
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

        var url = "http://someurl.com";
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        var savedLinkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class, url, lastScannedAt, createdAt);

        var subCreatedAt = randomDate();

        jdbcTemplate.update(insertSubscriptionSql, tgChatId, savedLinkId, subCreatedAt);
        var request = new SubscriptionIdInput(tgChatId, savedLinkId);


        // when
        var response = jooqSubscriptionRepository.findBySubscriptionId(request);


        // then
        assertThat(response, is(notNullValue()));
        assertThat(response.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(response.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(response.getCreatedAt().toEpochSecond(), is(equalTo(subCreatedAt.toEpochSecond())));

        var rs = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);

        assertThat(rs, is(notNullValue()));
        assertThat(rs, hasSize(1));

        var row = rs.get(0);

        assertThat(row, is(notNullValue()));
        assertThat(row.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(row.getLinkId(), is(equalTo(savedLinkId)));
        assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(subCreatedAt.toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testFindUnknownChat() {
        // given
        var tgChatId = randomId();
        var linkId = randomId();

        var request = new SubscriptionIdInput(tgChatId, linkId);

        // when
        var response = jooqSubscriptionRepository.findBySubscriptionId(request);


        // then
        assertThat(response, is(nullValue()));
    }
}

