package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddLinkInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.model.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.model.mapping.LinkOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ILinkRepository;

import java.time.OffsetDateTime;
import java.util.List;

import static ru.tinkoff.edu.java.scrapper.model.jooq.generated.Tables.LINK;

@Repository
@RequiredArgsConstructor
public class JooqLinkRepository implements ILinkRepository {

    private final DSLContext dslContext;
    private final LinkOutputMapper linkOutputMapper;

    @Override
    public Long save(AddLinkInput link) {
        var res = dslContext.insertInto(LINK, LINK.URL, LINK.LAST_SCANNED_AT, LINK.CREATED_AT)
                .values(link.url(), link.lastScannedAt(), link.createdAt())
                .onConflict(LINK.URL).doNothing()
                .returningResult(LINK.LINK_ID)
                .fetchOne();

        if (res == null) {
            return null;
        } else {
            return res.value1();
        }
    }

    @Override
    public LinkOutput remove(Long linkId) {

        LinkRecord res = dslContext.deleteFrom(LINK)
                .where(LINK.LINK_ID.eq(linkId))
                .returning(LINK.fields())
                .fetchOne();

        return linkOutputMapper.map(res);
    }

    @Override
    public List<LinkOutput> findAll() {
        var res = dslContext.select().from(LINK)
                .fetch();

        return linkOutputMapper.map(res.into(LinkRecord.class));
    }

    @Override
    public LinkOutput findById(Long linkId) {
        var res = dslContext.select().from(LINK)
                .where(LINK.LINK_ID.eq(linkId))
                .fetchOneInto(LinkRecord.class);

        if (res == null) {
            return null;
        } else {
            return linkOutputMapper.map(res);
        }
    }

    @Override
    public LinkOutput findByUrl(String url) {
        var res = dslContext.select().from(LINK)
                .where(LINK.URL.eq(url))
                .fetchOneInto(LinkRecord.class);

        if (res == null) {
            return null;
        } else {
            return linkOutputMapper.map(res);
        }
    }

    @Override
    public List<LinkOutput> findAllByLastScannedAtIsBefore(OffsetDateTime time) {
        var res = dslContext.select().from(LINK)
                .where(LINK.LAST_SCANNED_AT.lessThan(time))
                .fetchInto(LinkRecord.class);

        return linkOutputMapper.map(res);
    }

    @Override
    public void updateLastScannedAt(Long linkId, ILinkState state, OffsetDateTime scanTime) {
        dslContext.update(LINK)
                .set(LINK.LAST_SCANNED_AT, scanTime)
                .where(LINK.LINK_ID.eq(linkId))
                .execute();
    }
}
