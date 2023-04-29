package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.jooq.generated.tables.records.LinkRecord;

import java.util.List;

@Mapper(componentModel = "spring", uses = TimeMapper.class)
public interface LinkOutputMapper {

    LinkOutput map(LinkRecord link);

    List<LinkOutput> map(List<LinkRecord> links);

}
