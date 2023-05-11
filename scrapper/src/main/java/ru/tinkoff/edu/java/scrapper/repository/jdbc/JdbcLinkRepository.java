package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;

import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class JdbcLinkRepository implements ILinkRepository {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<LinkOutput> rowMapper;

    @Override
    public Long save(AddLinkInput link) {
        String insertSql = """
                insert into link(url, state, last_scanned_at, created_at)
                values (?, ?::json, ?, ?)
                on conflict do nothing
                returning link_id
                """;

        try {
            return jdbcTemplate.queryForObject(insertSql, Long.class,
                    link.url(), link.state().asJson(), link.lastScannedAt(), link.createdAt());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public LinkOutput remove(Long linkId) {
        String removeSql = """
                delete from link
                where link_id = ?
                returning *
                """;

        var rs = jdbcTemplate.query(removeSql, rowMapper, linkId);

        if (rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public List<LinkOutput> findAll() {
        String selectSql = """
                select * from link
                """;

        return jdbcTemplate.query(selectSql, rowMapper);
    }

    @Override
    public LinkOutput findById(Long linkId) {
        String selectSql = """
                select * from link
                where link_id = ?
                """;

        var rs = jdbcTemplate.query(selectSql, rowMapper, linkId);

        if (rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public LinkOutput findByUrl(String url) {
        String selectSql = """
                select * from link
                where url = ?
                """;

        var rs = jdbcTemplate.query(selectSql, rowMapper, url);

        if (rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public List<LinkOutput> findAllByLastScannedAtIsBefore(OffsetDateTime time) {
        String selectSql = """
                select * from link
                where last_scanned_at < ?::timestamptz
                """;

        return jdbcTemplate.query(selectSql, rowMapper, time.toString());
    }

    @Override
    public void updateLastScannedAt(Long linkId, ILinkState state, OffsetDateTime scanTime) {
        String updateSql = """
                update link
                set last_scanned_at = ?, state = ?::json
                where link_id = ?
                """;

        jdbcTemplate.update(updateSql, scanTime, state.asJson(), linkId);
    }
}
