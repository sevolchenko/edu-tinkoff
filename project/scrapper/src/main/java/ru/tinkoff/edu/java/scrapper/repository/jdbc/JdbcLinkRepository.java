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
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;

import java.time.Duration;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@RequiredArgsConstructor
@Repository
public class JdbcLinkRepository implements ILinkRepository {

    private final JdbcTemplate jdbcTemplate;

    private final Duration linkCheckDelay;

    @Override
    public Long add(SubscriptionInput request) {
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

        Long tgChatId = request.tgChatId();
        String url = request.link();

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
    public LinkOutput remove(SubscriptionInput request) {
        String removeSql = """
                delete from subscription using link
                where link.link_id = subscription.link_id and tg_chat_id = ? and url = ?
                returning link.link_id, url, last_scanned_at, subscription.created_at
                """;

        Long tgChatId = request.tgChatId();
        String link = request.link();

        var rs = jdbcTemplate.query(removeSql, new BeanPropertyRowMapper<>(LinkOutput.class), tgChatId, link);

        if (rs.isEmpty()) {
            throw new NoSuchLinkException(String.format("Link has not added to id %d yet: %s", tgChatId, link));
        }

        return rs.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<LinkOutput> findUncheckedLinks() {
        String uncheckedLinksWithTgChatsSql = """
                select s.tg_chat_id, s.link_id, l.url, s.created_at from subscription s, link l
                where s.link_id = l.link_id and l.last_scanned_at < (now() - 'INTERVAL ? seconds')
                """;

        Map<Long, SubscriptionOutput> links = new HashMap<>();
        jdbcTemplate.query(uncheckedLinksWithTgChatsSql, rs -> {

        }, linkCheckDelay.getSeconds());

        return List.of();
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
    public LinkOutput findById(SubscriptionInput request) {
        String findSql = """
                select s.link_id, url, last_scanned_at, s.created_at from subscription s, link l
                where s.link_id = l.link_id and url = ? and s.tg_chat_id = ?
                """;
        Long tgChatId = request.tgChatId();
        String url = request.link();

        var rs = jdbcTemplate.query(findSql, new BeanPropertyRowMapper<>(LinkOutput.class), url, tgChatId);

        if (rs.isEmpty()) {
            return null;
        }

        return rs.get(0);
    }
}
