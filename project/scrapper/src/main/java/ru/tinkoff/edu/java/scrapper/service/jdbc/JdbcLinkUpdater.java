package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;
import ru.tinkoff.edu.java.scrapper.service.interfaces.ILinkUpdater;

@Service
@RequiredArgsConstructor
public class JdbcLinkUpdater implements ILinkUpdater {

    private final ILinkRepository linkRepository;

    @Override
    public void update(Long linkId) {

    }
}
