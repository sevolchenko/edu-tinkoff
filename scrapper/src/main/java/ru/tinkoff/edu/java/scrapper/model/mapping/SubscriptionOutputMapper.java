package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.SubscriptionOutput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.SubscriptionRecord;

import java.util.List;

@Mapper(componentModel = "spring", uses = TimeMapper.class)
public interface SubscriptionOutputMapper {

    SubscriptionOutput map(SubscriptionRecord subscription);

    List<SubscriptionOutput> map(List<SubscriptionRecord> subscriptions);

    TgChatOutput map(TgChat tgChat);

}
