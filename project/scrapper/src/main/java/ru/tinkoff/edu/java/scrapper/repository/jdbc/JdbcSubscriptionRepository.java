package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddSubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionIdInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionIdOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ISubscriptionRepository;

import java.util.List;

@Primary
@Repository
@RequiredArgsConstructor
public class JdbcSubscriptionRepository implements ISubscriptionRepository {


    private final JdbcTemplate jdbcTemplate;
    
    private final RowMapper<SubscriptionOutput> rowMapper;
    private final RowMapper<SubscriptionIdOutput> idRowMapper;

    @Override
    public SubscriptionIdOutput save(AddSubscriptionInput subscription) {
        String insertSql = """
                insert into subscription(tg_chat_id, link_id, created_at)
                values (?, ?, ?)
                on conflict do nothing
                returning tg_chat_id, link_id
                """;

        var rs = jdbcTemplate.query(insertSql, idRowMapper,
                subscription.tgChatId(), subscription.linkId(),
                subscription.createdAt());

        if (rs.isEmpty()) {
            return null;
        } else  {
            return  rs.get(0);
        }
    }

    @Override
    public SubscriptionOutput remove(SubscriptionIdInput subscriptionId) {
        String removeSql = """
                delete from subscription
                where tg_chat_id = ? and link_id = ?
                returning tg_chat_id, link_id, created_at
                """;

        var rs = jdbcTemplate.query(removeSql, rowMapper,
                subscriptionId.tgChatId(), subscriptionId.linkId());

        if (rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }

    @Override
    public List<SubscriptionOutput> findAll() {
        String selectsSql = """
                select * from subscription
                """;

        return jdbcTemplate.query(selectsSql, rowMapper);
    }

    @Override
    public List<SubscriptionOutput> findAllByLinkId(Long linkId) {
        String selectsSql = """
                select * from subscription
                where link_id = ?
                """;

        return jdbcTemplate.query(selectsSql, rowMapper, linkId);
    }

    @Override
    public SubscriptionOutput findBySubscriptionId(SubscriptionIdInput subscriptionId) {

        String selectsSql = """
                select * from subscription
                where tg_chat_id = ? and link_id = ?
                """;

        var rs = jdbcTemplate.query(selectsSql, rowMapper, subscriptionId.tgChatId(), subscriptionId.linkId());

        if (rs.isEmpty()) {
            return null;
        } else {
            return rs.get(0);
        }
    }
}
