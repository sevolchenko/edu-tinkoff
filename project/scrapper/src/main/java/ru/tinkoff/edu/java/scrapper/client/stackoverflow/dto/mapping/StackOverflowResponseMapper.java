package ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowAPIResponse;
import ru.tinkoff.edu.java.scrapper.client.stackoverflow.dto.StackOverflowQuestionResponse;

@Mapper(componentModel = "spring")
public interface StackOverflowResponseMapper {

    @Mapping(target = "state.lastActivityDate", source = "lastActivityDate")
    @Mapping(target = "state.answerCount", source = "answerCount")
    StackOverflowQuestionResponse map(StackOverflowAPIResponse apiResponse);
}
