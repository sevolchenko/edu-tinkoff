package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.TgChat;

@Mapper
public interface TgChatMapper {

    TgChatOutput toOutput(TgChat tgChat);

}
