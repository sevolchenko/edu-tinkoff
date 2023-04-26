package ru.tinkoff.edu.java.scrapper.model.mapping;

import org.mapstruct.Mapper;
import ru.tinkoff.edu.java.scrapper.model.dto.internal.output.LinkOutput;
import ru.tinkoff.edu.java.scrapper.model.entity.Link;

@Mapper(componentModel = "spring", uses = TimeMapper.class)
public interface LinkMapper {

    LinkOutput toOutput(Link link);

}
