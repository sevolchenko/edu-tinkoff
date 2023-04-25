package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;

@Repository
public interface JpaTgChatRepository extends JpaRepository<TgChat, Long> {
}
