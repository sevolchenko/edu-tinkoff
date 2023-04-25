package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyAddedLinkException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchLinkException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkWithChatsOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Repository
public class JdbcLinkRepository implements ILinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long add(SubscriptionInput input) {
        String linkSql = """
                insert into link(url, last_scanned_at, created_at)
                values (?, now(), now())
                on conflict (url) do update set last_scanned_at = now()
                returning link_id
                """;

        String subscriptionSql = """
                insert into subscription(tg_chat_id, link_id, created_at)
                values (?, ?, now())
                on conflict do nothing
                returning link_id
                """;

        Long tgChatId = input.tgChatId();
        String url = input.link();

        Long linkId = jdbcTemplate.queryForObject(linkSql, Long.class, url);

        try {
            jdbcTemplate.queryForObject(subscriptionSql, Long.class, tgChatId, linkId);
            return linkId;

        } catch (EmptyResultDataAccessException ex) {
            throw new AlreadyAddedLinkException(String.format("Link already added to id %d: %s", tgChatId, url));
        } catch (DataIntegrityViolationException ex) {
            throw new NoSuchChatException(String.format("There is no chat with id %d", tgChatId));
        }
    }

    @Override
    public LinkOutput remove(SubscriptionInput input) {
        String removeSql = """
                delete from subscription using link
                where link.link_id = subscription.link_id and tg_chat_id = ? and url = ?
                returning link.link_id, url, last_scanned_at, subscription.created_at
                """;

        Long tgChatId = input.tgChatId();
        String link = input.link();

        var rs = jdbcTemplate.query(removeSql, new BeanPropertyRowMapper<>(LinkOutput.class), tgChatId, link);

        if (rs.isEmpty()) {
            throw new NoSuchLinkException(String.format("Link has not added to id %d yet: %s", tgChatId, link));
        }

        return rs.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LinkWithChatsOutput> findUncheckedLinks(Duration linkCheckDelay) {
        String uncheckedLinksWithTgChatsSql = """
                select s.tg_chat_id, s.link_id, l.url, l.last_scanned_at from subscription s, link l
                where s.link_id = l.link_id and l.last_scanned_at < ((now() - make_interval(secs => ?)))
                """;

        Map<Long, LinkWithChatsOutput> links = new HashMap<>();
        jdbcTemplate.query(uncheckedLinksWithTgChatsSql, rs -> {
            Long tgChatId = rs.getLong("tg_chat_id");
            Long linkId = rs.getLong("link_id");
            String url = rs.getString("url");
            OffsetDateTime lastScannedAt = getOffsetDateTime(rs, "last_scanned_at");

            LinkWithChatsOutput output = links.computeIfAbsent(linkId, i ->
                    new LinkWithChatsOutput(i, url, lastScannedAt, new ArrayList<>())
            );

            output.tgChatIds().add(tgChatId);

        }, linkCheckDelay.getSeconds());

        return links.values();
    }


    @Override
    public void updateLastScanTime(Long linkId) {
        String updateScanSql = """
                update link set last_scanned_at = now()
                where link.link_id = ?
                returning link_id
                """;

        Long resLinkId = jdbcTemplate.queryForObject(updateScanSql, Long.class, linkId);

        if (resLinkId == null) {
            throw new NoSuchLinkException(String.format("There is no link with id %d", linkId));
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LinkOutput> findAll(Long tgChatId) {
        String findAllSql = """
                select s.link_id, url, last_scanned_at, s.created_at from subscription s, link l
                where s.link_id = l.link_id and s.tg_chat_id = ?
                """;

        return jdbcTemplate.query(findAllSql, new BeanPropertyRowMapper<>(LinkOutput.class), tgChatId);
    }

    @Override
    @Transactional(readOnly = true)
    public LinkOutput findById(SubscriptionInput input) {
        String findSql = """
                select s.link_id, url, last_scanned_at, s.created_at from subscription s, link l
                where s.link_id = l.link_id and url = ? and s.tg_chat_id = ?
                """;
        Long tgChatId = input.tgChatId();
        String url = input.link();

        var rs = jdbcTemplate.query(findSql, new BeanPropertyRowMapper<>(LinkOutput.class), url, tgChatId);

        if (rs.isEmpty()) {
            return null;
        }

        return rs.get(0);
    }

    private static OffsetDateTime getOffsetDateTime(ResultSet rs, String columnLabel) {
        try {
            return OffsetDateTime.of(rs.getObject(columnLabel, Timestamp.class).toLocalDateTime(), ZoneOffset.UTC);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
