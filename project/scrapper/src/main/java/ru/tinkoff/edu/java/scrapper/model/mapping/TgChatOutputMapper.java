package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.TgChatRecord;

import java.util.List;

@Mapper(componentModel = "spring", uses = TimeMapper.class)
public interface TgChatOutputMapper {

    TgChatOutput map(TgChatRecord tgChat);

    List<TgChatOutput> map(List<TgChatRecord> tgChats);

}
