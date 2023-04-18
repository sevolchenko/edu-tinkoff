package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.exception.AlreadyRegisteredChatException;
import ru.tinkoff.edu.java.scrapper.exception.NoSuchChatException;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.RegisterTgChatRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.TgChatResponse;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;

import java.util.Collection;
import java.util.List;

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

        List<Long> tempId = jdbcTemplate.query(addSql, (rs, rn) -> rs.getLong("tg_chat_id"), tgChatId, username);

        if (tempId.isEmpty()) {
            throw new AlreadyRegisteredChatException(String.format("Chat with id %d already registered", tgChatId));
        }

        return tempId.get(0);
    }

    @Override
    public TgChatResponse remove(Long tgChatId) {
        String removeSql = """
                delete from tg_chat where tg_chat_id = ?
                returning (tg_chat_id, username, registered_at)
                """;

        final TgChatResponse response = jdbcTemplate.queryForObject(removeSql, TgChatResponse.class, tgChatId);

        if (response == null) {
            throw new NoSuchChatException(String.format("There is no chat with id %d", tgChatId));
        }

        return response;
    }

    @Override
    public Collection<TgChatResponse> findAll() {
        return null;
    }
}
