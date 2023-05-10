package ru.tinkoff.edu.java.scrapper.repository.interfaces;

import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;

import java.time.OffsetDateTime;
import java.util.List;

public interface ILinkRepository {

    Long save(AddLinkInput link);

    LinkOutput remove(Long linkId);

    List<LinkOutput> findAll();

    LinkOutput findById(Long linkId);

    LinkOutput findByUrl(String url);

    List<LinkOutput> findAllByLastScannedAtIsBefore(OffsetDateTime time);

    void updateLastScannedAt(Long linkId, ILinkState state, OffsetDateTime scanTime);

}
