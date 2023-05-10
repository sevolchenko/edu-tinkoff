package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddSubscriptionInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.SubscriptionIdInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionIdOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.SubscriptionRecord;
import ru.tinkoff.edu.java.scrapper.model.mapping.SubscriptionOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ISubscriptionRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.model.jooq.generated.Tables.SUBSCRIPTION;

@Repository
@RequiredArgsConstructor
public class JooqSubscriptionRepository implements ISubscriptionRepository {


    private final DSLContext dslContext;
    private final SubscriptionOutputMapper subscriptionOutputMapper;

    @Override
    public SubscriptionIdOutput save(AddSubscriptionInput subscription) {

        var res = dslContext.insertInto(SUBSCRIPTION, SUBSCRIPTION.fields())
                .values(subscription.tgChatId(), subscription.linkId(), subscription.createdAt())
                .onConflict().doNothing()
                .returningResult(SUBSCRIPTION.TG_CHAT_ID, SUBSCRIPTION.LINK_ID)
                .fetchOne();

        if (res == null) {
            return null;
        } else {
            var sub = new SubscriptionIdOutput();
            sub.setTgChatId(res.value1());
            sub.setLinkId(res.value2());
            return sub;
        }
    }

    @Override
    public SubscriptionOutput remove(SubscriptionIdInput subscriptionId) {

        SubscriptionRecord res = dslContext.deleteFrom(SUBSCRIPTION)
                .where(SUBSCRIPTION.TG_CHAT_ID.eq(subscriptionId.tgChatId()))
                .and(SUBSCRIPTION.LINK_ID.eq(subscriptionId.linkId()))
                .returning(SUBSCRIPTION.fields())
                .fetchOne();

        return subscriptionOutputMapper.map(res);
    }

    @Override
    public List<SubscriptionOutput> findAll() {
        var res = dslContext.select().from(SUBSCRIPTION)
                .fetch();

        return subscriptionOutputMapper.map(res.into(SubscriptionRecord.class));
    }

    @Override
    public List<SubscriptionOutput> findAllByLinkId(Long linkId) {
        var res = dslContext.select().from(SUBSCRIPTION)
                .where(SUBSCRIPTION.LINK_ID.eq(linkId))
                .fetchInto(SubscriptionRecord.class);

        return subscriptionOutputMapper.map(res);
    }

    @Override
    public SubscriptionOutput findBySubscriptionId(SubscriptionIdInput subscriptionId) {

        var res = dslContext.select().from(SUBSCRIPTION)
                .where(SUBSCRIPTION.TG_CHAT_ID.eq(subscriptionId.tgChatId()))
                .and(SUBSCRIPTION.LINK_ID.eq(subscriptionId.linkId()))
                .fetchOneInto(SubscriptionRecord.class);

        return subscriptionOutputMapper.map(res);

    }
}
