package ru.tinkoff.edu.java.scrapper.repository.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.input.AddTgChatInput;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.TgChatOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.TgChatRecord;
import ru.tinkoff.edu.java.scrapper.model.mapping.TgChatOutputMapper;
import ru.tinkoff.edu.java.scrapper.repository.interfaces.ITgChatRepository;

import java.util.List;

import static ru.tinkoff.edu.java.scrapper.model.jooq.generated.Tables.TG_CHAT;

@Repository
@RequiredArgsConstructor
public class JooqTgChatRepository implements ITgChatRepository {

    private final DSLContext dslContext;
    private final TgChatOutputMapper tgChatOutputMapper;

    @Override
    public Long save(AddTgChatInput tgChat) {
        var res = dslContext.insertInto(TG_CHAT, TG_CHAT.fields())
                .values(tgChat.tgChatId(), tgChat.username(), tgChat.registeredAt())
                .returningResult(TG_CHAT.TG_CHAT_ID)
                .fetchOne();

        if (res == null) {
            return null;
        } else {
            return res.value1();
        }
    }

    @Override
    public TgChatOutput remove(Long tgChatId) {
        TgChatRecord res = dslContext.deleteFrom(TG_CHAT)
                .where(TG_CHAT.TG_CHAT_ID.eq(tgChatId))
                .returning(TG_CHAT.fields())
                .fetchOne();

        return tgChatOutputMapper.map(res);
    }

    @Override
    public List<TgChatOutput> findAll() {
        var res = dslContext.select().from(TG_CHAT)
                .fetch();

        return tgChatOutputMapper.map(res.into(TgChatRecord.class));
    }

    @Override
    public TgChatOutput findById(Long tgChatId) {
        var res = dslContext.select().from(TG_CHAT)
                .where(TG_CHAT.TG_CHAT_ID.eq(tgChatId))
                .fetchOneInto(TgChatRecord.class);

        if (res == null) {
            return null;
        } else {
            return tgChatOutputMapper.map(res);
        }
    }

}
