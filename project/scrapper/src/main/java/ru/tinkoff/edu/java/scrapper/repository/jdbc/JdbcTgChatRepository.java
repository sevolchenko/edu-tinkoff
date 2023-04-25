package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.RegisterTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;

import java.util.Collection;

@Transactional
@RequiredArgsConstructor
@Repository
public class JdbcTgChatRepository implements ITgChatRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long add(RegisterTgChatInput input) {
        String addSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, now())
                on conflict (tg_chat_id)
                do nothing
                returning tg_chat_id
                """;

        var tgChatId = input.tgChatId();
        var username = input.username();

        try {
            return jdbcTemplate.queryForObject(addSql, Long.class, tgChatId, username);
        } catch (EmptyResultDataAccessException ex) {
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", tgChatId));
        }
    }

    @Override
    public TgChatOutput remove(Long tgChatId) {
        String removeSql = """
                delete from tg_chat where tg_chat_id = ?
                returning tg_chat_id, username, registered_at
                """;

        var rs = jdbcTemplate.query(removeSql, new BeanPropertyRowMapper<>(TgChatOutput.class), tgChatId);

        if (rs.isEmpty()) {
            throw new NoSuchChatException(String.format("There is no chat with id %d", tgChatId));
        }

        return rs.get(0);
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<TgChatOutput> findAll() {
        String selectSql = "select tg_chat_id, username, registered_at from tg_chat";

        return jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(TgChatOutput.class));
    }

    @Override
    @Transactional(readOnly = true)
    public TgChatOutput findByTgChatId(Long tgChatId) {
        String selectSql = "select tg_chat_id, username, registered_at from tg_chat where tg_chat_id = ?";

        var rs = jdbcTemplate.query(selectSql, new BeanPropertyRowMapper<>(TgChatOutput.class), tgChatId);

        if (rs.isEmpty()) {
            return null;
        }

        return rs.get(0);
    }
}
