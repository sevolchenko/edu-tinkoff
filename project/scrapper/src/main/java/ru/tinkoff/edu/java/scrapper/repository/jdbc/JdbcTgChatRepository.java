package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;

import java.util.List;

@Primary
@RequiredArgsConstructor
@Repository
public class JdbcTgChatRepository implements ITgChatRepository {

    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<TgChatOutput> rowMapper;

    @Override
    public Long save(AddTgChatInput tgChat) {
        String insertSql = """
                insert into tg_chat(tg_chat_id, username, registered_at)
                values (?, ?, ?)
                on conflict (tg_chat_id) do nothing
                returning tg_chat_id
                """;
        try {
            return jdbcTemplate.queryForObject(insertSql, Long.class, tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt());
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public TgChatOutput remove(Long tgChatId) {
        String deleteSql = """
                delete from tg_chat
                where tg_chat_id = ?
                returning tg_chat_id, username, registered_at
                """;

        try {
            return jdbcTemplate.queryForObject(deleteSql, rowMapper, tgChatId);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List<TgChatOutput> findAll() {
        String selectSql = """
                select * from tg_chat
                """;
        return jdbcTemplate.query(selectSql, rowMapper);
    }

    @Override
    public TgChatOutput findById(Long tgChatId) {
        String selectSql = """
                select * from tg_chat
                where tg_chat_id = ?
                """;

        try {
            return jdbcTemplate.queryForObject(selectSql, rowMapper, tgChatId);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

}
