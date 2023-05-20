package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.exception.AlreadySubscribedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchSubscriptionException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaSubscriptionRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaTgChatRepository;
import ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData;
import ru.tinkoff.edu.java.scrapper.reposotory.data.TestTgChatData;

import java.net.URI;
import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestDatesData.randomDate;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData.randomId;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData.randomState;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestSubscriptionData.random;

@SpringBootTest
@ActiveProfiles({"test", "test-jpa"})
public class JpaLinkServiceTest extends IntegrationEnvironment {

    private JpaLinkService linkService;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JpaSubscriptionRepository subscriptionRepository;

    @Autowired
    private JpaTgChatRepository tgChatRepository;

    @Autowired
    private LinkOutputMapper linkOutputMapper;

    private LinkProcessor linkProcessor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RowMapper<LinkOutput> linkRowMapper;
    @Autowired
    private RowMapper<SubscriptionOutput> subscriptionRowMapper;

    private final String selectLinkSql = """
            select * from link
            """;

    private final String selectSubscriptionSql = """
            select * from subscription
            """;
    private final String insertTgChatSql = """
            insert into tg_chat(tg_chat_id, username, registered_at)
            values (?, ?, ?)
            """;

    private final String insertLinkSql = """
            insert into link(url, state, last_scanned_at, created_at)
            values (?, ?::json, ?, ?)
            on conflict (url) do update set url = excluded.url
            returning link_id
            """;

    private final String insertSubSql = """
            insert into subscription(tg_chat_id, link_id, created_at)
            values (?, ?, ?)
            """;

    @BeforeEach
    public void setUp() {
        linkProcessor = Mockito.mock(LinkProcessor.class);
        linkService = new JpaLinkService(
                linkRepository,
                subscriptionRepository,
                tgChatRepository,
                linkProcessor,
                linkOutputMapper
        );
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidExistingInDbLink() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url.toString(), state.asJson(), lastScannedAt, createdAt);

        // when
        var response = linkService.add(tgChatId, url);
        linkRepository.flush();
        subscriptionRepository.flush();


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response.getLinkId(), is(equalTo(linkId)));
        assertThat(response.getUrl(), is(equalTo(url.toString())));
        assertThat(response.getState(), is(equalTo(state)));
        assertThat(response.getLastScannedAt().toEpochSecond(),
                is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(),
                is(equalTo(createdAt.toEpochSecond())));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);

        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(1));

        var linkRow = linksRS.get(0);

        assertThat(response, is(notNullValue()));

        assertThat(response.getLinkId(), is(equalTo(linkRow.getLinkId())));
        assertThat(response.getUrl(), is(equalTo(linkRow.getUrl())));
        assertThat(response.getState(), is(equalTo(linkRow.getState())));
        assertThat(response.getLastScannedAt().toEpochSecond(),
                is(equalTo(linkRow.getLastScannedAt().toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(),
                is(equalTo(linkRow.getCreatedAt().toEpochSecond())));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(1));

        var subRow = subRS.get(0);

        assertThat(subRow.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(subRow.getLinkId(), is(equalTo(linkId)));
        assertThat(subRow.getCreatedAt().toEpochSecond(),
                is(lessThanOrEqualTo(OffsetDateTime.now().toEpochSecond())));
    }


    @Transactional
    @Rollback
    @Test
    void testAddValidNotExistingInDbLink() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");
        var state = randomState();

        Mockito.when(linkProcessor.getState(url))
                .thenReturn(state);

        // when
        var response = linkService.add(tgChatId, url);
        linkRepository.flush();
        subscriptionRepository.flush();


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response.getUrl(), is(equalTo(url.toString())));
        assertThat(response.getState(), is(equalTo(state)));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);

        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(1));

        var linkRow = linksRS.get(0);

        assertThat(response, is(notNullValue()));

        assertThat(response.getLinkId(), is(equalTo(linkRow.getLinkId())));
        assertThat(response.getUrl(), is(equalTo(linkRow.getUrl())));
        assertThat(response.getState(), is(equalTo(linkRow.getState())));
        assertThat(response.getLastScannedAt().toEpochSecond(),
                is(equalTo(linkRow.getLastScannedAt().toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(),
                is(equalTo(linkRow.getCreatedAt().toEpochSecond())));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(1));

        var subRow = subRS.get(0);

        assertThat(subRow.getTgChatId(), is(equalTo(tgChatId)));
        assertThat(subRow.getLinkId(), is(equalTo(response.getLinkId())));
        assertThat(subRow.getCreatedAt().toEpochSecond(),
                is(lessThanOrEqualTo(OffsetDateTime.now().toEpochSecond())));
    }

    @Transactional
    @Rollback
    @Test
    void testAddUnknownTgChat() {
        // given
        var tgChatId = randomId();
        var url = URI.create("https://vk.com");


        // when
        var ex = assertThrows(NoSuchChatException.class, () ->
                linkService.add(tgChatId, url));


        // then
        assertThat(ex.getMessage(), is(equalTo("There is no chat with id %d".formatted(tgChatId))));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(0));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(0));
    }

    @Transactional
    @Rollback
    @Test
    void testAddValidExistingInDbSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url.toString(), state.asJson(), lastScannedAt, createdAt);

        var subCreatedAt = OffsetDateTime.now();
        jdbcTemplate.update(insertSubSql, tgChatId, linkId, subCreatedAt);


        // when
        var ex = assertThrows(AlreadySubscribedLinkException.class, () ->
                linkService.add(tgChatId, url));


        // then
        assertThat(ex.getMessage(), is(equalTo(
                "Subscription to link %s for chat id %d already added".formatted(url, tgChatId))));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(1));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(1));
    }


    @Transactional
    @Rollback
    @Test
    void testRemoveUnknownTgChat() {
        // given
        var tgChatId = randomId();
        var url = URI.create("https://vk.com");


        // when
        var ex = assertThrows(NoSuchChatException.class, () ->
                linkService.remove(tgChatId, url));


        // then
        assertThat(ex.getMessage(), is(equalTo("There is no chat with id %d".formatted(tgChatId))));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(0));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testRemoveUnknownLink() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");


        // when
        var ex = assertThrows(NoSuchLinkException.class, () ->
                linkService.remove(tgChatId, url));


        // then
        assertThat(ex.getMessage(), is(equalTo("There is no link with url %s".formatted(url))));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(0));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testRemoveNotExistingSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url.toString(), state.asJson(), lastScannedAt, createdAt);

        // when
        var ex = assertThrows(NoSuchSubscriptionException.class, () ->
                linkService.remove(tgChatId, url));


        // then
        assertThat(ex.getMessage(), is(equalTo(
                "Subscription to link %s for chat id %d is not exists".formatted(url.toString(), tgChatId))));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(1));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(0));
    }

    @Transactional
    @Rollback
    @Test
    void testRemoveExistingSub() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);

        var url = URI.create("https://vk.com");
        var state = randomState();
        var lastScannedAt = randomDate();
        var createdAt = OffsetDateTime.now();
        Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                url.toString(), state.asJson(), lastScannedAt, createdAt);

        var subCreatedAt = OffsetDateTime.now();
        jdbcTemplate.update(insertSubSql, tgChatId, linkId, subCreatedAt);


        // when
        var response = linkService.remove(tgChatId, url);
        linkRepository.flush();
        subscriptionRepository.flush();


        // then
        assertThat(response, is(notNullValue()));

        assertThat(response.getLinkId(), is(equalTo(linkId)));
        assertThat(response.getUrl(), is(equalTo(url.toString())));
        assertThat(response.getState(), is(equalTo(state)));
        assertThat(response.getLastScannedAt().toEpochSecond(),
                is(equalTo(lastScannedAt.toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(),
                is(equalTo(createdAt.toEpochSecond())));

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        assertThat(linksRS, is(notNullValue()));
        assertThat(linksRS, hasSize(1));

        var subRS = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        assertThat(subRS, is(notNullValue()));
        assertThat(subRS, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testListAllForUnknownChat() {
        // given
        var tgChatId = randomId();


        // when
        var ex = assertThrows(NoSuchChatException.class, () ->
                linkService.listAllForChat(tgChatId));


        // then
        assertThat(ex.getMessage(), is(equalTo("There is no chat with id %d".formatted(tgChatId))));
    }

    @Transactional
    @Rollback
    @Test
    void testListAllEmptyForChat() {
        // given
        var tgChatId = randomId();
        var username = "username";
        var registeredAt = randomDate();

        jdbcTemplate.update(insertTgChatSql, tgChatId, username, registeredAt);


        // when
        var responses = linkService.listAllForChat(tgChatId);


        // then
        assertThat(responses, is(notNullValue()));

        assertThat(responses, hasSize(0));
    }


    @Transactional
    @Rollback
    @Test
    void testListAllForValidChat() {
        // given
        var tgChats = TestTgChatData.stabValidResponse();
        var links = TestLinkData.stabValidResponse();

        tgChats.forEach(tgChat -> {
            var link = links.stream().findAny().get();
            jdbcTemplate.update(insertTgChatSql,
                    tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
            Long linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                    link.url(), link.state().asJson(), link.lastScannedAt(), link.createdAt());

            var subCreatedAt = randomDate();
            jdbcTemplate.update(insertSubSql, tgChat.tgChatId(), linkId, subCreatedAt);
        });

        var chosenIdx = random(tgChats.size());

        // when
        var responses = linkService.listAllForChat(tgChats.get(chosenIdx).tgChatId());


        // then
        assertThat(responses, is(notNullValue()));
        assertThat(responses, hasSize(1));

        var response = responses.stream().findFirst().get();
        assertThat(response, is(notNullValue()));

        var subs = jdbcTemplate.query(selectSubscriptionSql, subscriptionRowMapper);
        var sub = subs.stream().filter(subRow -> subRow.getTgChatId().equals(tgChats.get(chosenIdx).tgChatId()))
                .findFirst().get();

        var linksRS = jdbcTemplate.query(selectLinkSql, linkRowMapper);
        var link = linksRS.stream()
                .filter(linkRow -> linkRow.getLinkId().equals(sub.getLinkId()))
                .findFirst().get();

        assertThat(response.getLinkId(), is(equalTo(link.getLinkId())));
        assertThat(response.getUrl(), is(equalTo(link.getUrl())));
        assertThat(response.getState(), is(equalTo(link.getState())));
        assertThat(response.getLastScannedAt().toEpochSecond(),
                is(equalTo(link.getLastScannedAt().toEpochSecond())));
        assertThat(response.getCreatedAt().toEpochSecond(),
                is(equalTo(link.getCreatedAt().toEpochSecond())));
    }

}