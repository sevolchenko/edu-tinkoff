package ru.tinkoff.edu.java.scrapper.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.entity.Link;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface JpaLinkRepository extends JpaRepository<Link, Long> {

    boolean existsByUrlEquals(String url);

    Optional<Link> findByUrlEquals(String url);

    List<Link> findAllByLastScannedAtIsBefore(Instant time);

}
