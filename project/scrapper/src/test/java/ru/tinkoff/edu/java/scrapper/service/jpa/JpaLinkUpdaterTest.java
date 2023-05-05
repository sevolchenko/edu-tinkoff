package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.StreamUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.IntegrationEnvironment;
import ru.tinkoff.edu.java.scrapper.client.github.dto.GitHubLinkState;
import ru.tinkoff.edu.java.scrapper.component.processor.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.component.producer.INotificationProducer;
import ru.tinkoff.edu.java.scrapper.component.producer.dto.LinkUpdateRequest;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;

import java.time.Duration;
import java.time.OffsetDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static ru.tinkoff.edu.java.scrapper.reposotory.data.TestLinkData.*;

@SpringBootTest
@ActiveProfiles({"test", "test-jpa"})
public class JpaLinkUpdaterTest extends IntegrationEnvironment {


    private JpaLinkUpdater linkUpdater;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private Duration linkCheckDelay;
    private LinkProcessor linkProcessor;

    private INotificationProducer notificationProducer;

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

    @BeforeEach
    public void setUp() {
        linkProcessor = Mockito.mock(LinkProcessor.class);
        notificationProducer = Mockito.mock(INotificationProducer.class);

        linkUpdater = new JpaLinkUpdater(
                linkRepository,
                linkCheckDelay,
                linkProcessor,
                notificationProducer
        );
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateNothingToScan() {
        // given
        var list = stabValidResponse();
        var addRequestsList = list.stream()
                .map(request -> {
                    var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            request.url(), request.state().asJson(),
                            OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)),
                            request.createdAt());

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setState(request.state());
                    output.setLastScannedAt(OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)));
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();


        // when
        linkUpdater.update();


        // then

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        StreamUtils.zip(rs.stream(), addRequestsList.stream(),
                (row, addLink) -> {
                    assertThat(row.getLinkId(), is(equalTo(addLink.getLinkId())));
                    assertThat(row.getUrl(), is(equalTo(addLink.getUrl())));
                    assertThat(row.getState(), is(equalTo(addLink.getState())));
                    assertThat(row.getLastScannedAt().toEpochSecond(), is(equalTo(addLink.getLastScannedAt().toEpochSecond())));
                    assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(addLink.getCreatedAt().toEpochSecond())));
                    return null;
                });
    }

    @Transactional
    @Rollback
    @Test
    void testUpdateWithChanges() {
        // given
        var list = stabValidResponse();
        var addRequestsList = list.stream()
                .map(request -> {
                    var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            request.url(), request.state().asJson(),
                            OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)),
                            request.createdAt());

                    var newState = randomState();
                    when(linkProcessor.getState(ArgumentMatchers.any()))
                            .thenReturn(randomUpdatedState((GitHubLinkState) request.state()));
                    doNothing().when(notificationProducer)
                            .sendUpdate(ArgumentMatchers.any(LinkUpdateRequest.class));

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setState(newState);
                    output.setLastScannedAt(OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)));
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        // when
        linkUpdater.update();
        linkRepository.flush();

        // then

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        StreamUtils.zip(rs.stream(), addRequestsList.stream(),
                (row, addLink) -> {
                    assertThat(row.getLinkId(), is(equalTo(addLink.getLinkId())));
                    assertThat(row.getUrl(), is(equalTo(addLink.getUrl())));
                    assertThat(row.getState(), is(equalTo(addLink.getState())));
                    assertThat(row.getLastScannedAt().toEpochSecond(), is(lessThanOrEqualTo(OffsetDateTime.now().toEpochSecond())));
                    assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(addLink.getCreatedAt().toEpochSecond())));
                    return null;
                });
    }


    @Transactional
    @Rollback
    @Test
    void testUpdateWithoutChanges() {
        // given
        var list = stabValidResponse();
        var addRequestsList = list.stream()
                .map(request -> {
                    var linkId = jdbcTemplate.queryForObject(insertLinkSql, Long.class,
                            request.url(), request.state().asJson(),
                            OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)),
                            request.createdAt());

                    var newState = randomState();
                    when(linkProcessor.getState(ArgumentMatchers.any()))
                            .thenReturn(randomUpdatedState((GitHubLinkState) request.state()));
                    doNothing().when(notificationProducer)
                            .sendUpdate(ArgumentMatchers.any(LinkUpdateRequest.class));

                    var output = new LinkOutput();
                    output.setLinkId(linkId);
                    output.setUrl(request.url());
                    output.setState(newState);
                    output.setLastScannedAt(OffsetDateTime.now().minus(linkCheckDelay).plus(Duration.ofDays(1)));
                    output.setCreatedAt(request.createdAt());
                    return output;
                })
                .toList();

        // when
        linkUpdater.update();
        linkRepository.flush();

        // then

        var rs = jdbcTemplate.query(selectLinkSql, rowMapper);

        StreamUtils.zip(rs.stream(), addRequestsList.stream(),
                (row, addLink) -> {
                    assertThat(row.getLinkId(), is(equalTo(addLink.getLinkId())));
                    assertThat(row.getUrl(), is(equalTo(addLink.getUrl())));
                    assertThat(row.getState(), is(equalTo(addLink.getState())));
                    assertThat(row.getLastScannedAt().toEpochSecond(), is(lessThanOrEqualTo(OffsetDateTime.now().toEpochSecond())));
                    assertThat(row.getCreatedAt().toEpochSecond(), is(equalTo(addLink.getCreatedAt().toEpochSecond())));
                    return null;
                });
    }

}
