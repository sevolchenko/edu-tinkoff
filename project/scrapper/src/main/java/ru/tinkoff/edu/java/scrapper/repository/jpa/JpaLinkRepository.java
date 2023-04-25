package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.entity.Link;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {
}
