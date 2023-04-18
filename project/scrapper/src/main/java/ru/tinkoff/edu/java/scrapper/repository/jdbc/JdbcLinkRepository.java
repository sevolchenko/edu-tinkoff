package ru.tinkoff.edu.java.scrapper.repository.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.dto.request.SubscriptionRequest;
import ru.tinkoff.edu.java.scrapper.repository.dto.response.SubscriptionResponse;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;

import java.util.Collection;

@Transactional
@RequiredArgsConstructor
public class JdbcLinkRepository implements ILinkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long add(SubscriptionRequest request) {
        return null;
    }

    @Override
    public SubscriptionResponse remove(SubscriptionRequest request) {
        return null;
    }

    @Override
    public Collection<SubscriptionResponse> findAll(Long tgChatId) {
        return null;
    }
}
