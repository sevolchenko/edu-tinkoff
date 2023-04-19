package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.TgChatResponse;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;

import java.util.Collection;

@Transactional
@RequiredArgsConstructor
@Repository
public class JdbcTgChatRepository implements ITgChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long add(RegisterTgChatRequest request) {
        String addSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, now())
                on conflict (tg_chat_id)
                do nothing
                returning tg_chat_id
                """;

        var tgChatId = request.tgChatId();
        var username = request.username();

        try {
            return jdbcTemplate.queryForObject(addSql, Long.class, tgChatId, username);
        } catch (EmptyResultDataAccessException ex) {
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", tgChatId));
        }
    }

    @Override
    public TgChatResponse remove(Long tgChatId) {
        String removeSql = """
                delete from tg_chat where tg_chat_id = ?
                returning tg_chat_id, username, registered_at
                """;

        var rs = jdbcTemplate.query(removeSql, new BeanPropertyRowMapper<>(TgChatResponse.class), tgChatId);

        if (rs.isEmpty()) {
            throw new NoSuchChatException(String.format("There is no chat with id %d", tgChatId));
        }

        return rs.get(0);
    }

    @Override
    public Collection<TgChatResponse> findAll() {
        String selectSql = "select tg_chat_id, username, registered_at from tg_chat";

        return jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(TgChatResponse.class));
    }

    @Override
    public TgChatResponse findByTgChatId(Long tgChatId) {
        String selectSql = "select tg_chat_id, username, registered_at from tg_chat where tg_chat_id = ?";

        var rs = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(TgChatResponse.class), tgChatId);

        if (rs.isEmpty()) {
            return null;
        }

        return rs.get(0);
    }
}
