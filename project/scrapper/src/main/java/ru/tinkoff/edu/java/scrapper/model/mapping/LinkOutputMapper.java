package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.jooq.JSON;
import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.linkstate.ILinkState;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.LinkRecord;
import ru.tinkoff.edu.java.scrapper.util.ObjectMapperUtil;

import java.util.List;

@Mapper(componentModel = "spring", uses = TimeMapper.class)
public interface LinkOutputMapper {

    LinkOutput map(LinkRecord link);

    List<LinkOutput> map(List<LinkRecord> links);

    default ILinkState map(JSON json) {
        return ObjectMapperUtil.readValue(json.data(), ILinkState.class);
    }

}
