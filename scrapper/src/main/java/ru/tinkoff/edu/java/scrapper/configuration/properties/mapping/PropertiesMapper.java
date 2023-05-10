package ru.tinkoff.edu.java.scrapper.configuration.properties.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.tinkoff.edu.java.scrapper.configuration.properties.ClientUrlProperties;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
public interface PropertiesMapper {

    @Mapping(target = "tgBotClientUrl",
            defaultExpression = "java(ClientUrlProperties.DEFAULT_TG_BOT_API)")
    @Mapping(target = "gitHubClientUrl",
            defaultExpression = "java(ClientUrlProperties.DEFAULT_GIT_HUB_API)")
    @Mapping(target = "stackOverflowClientUrl",
            defaultExpression = "java(ClientUrlProperties.DEFAULT_STACK_OVERFLOW_API)")
    ClientUrlProperties fillDefaults(ClientUrlProperties clientUrlProperties);

}
