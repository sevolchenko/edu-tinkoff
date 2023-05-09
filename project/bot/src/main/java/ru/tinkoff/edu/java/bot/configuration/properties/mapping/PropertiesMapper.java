package ru.tinkoff.edu.java.bot.configuration.properties.mapping;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;
import ru.tinkoff.edu.java.bot.configuration.properties.ClientUrlProperties;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates.CommandsMessageTemplates;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates.ErrorsMessageTemplates;
import ru.tinkoff.edu.java.bot.configuration.properties.MessageTemplates.EventsMessageTemplates;

@Mapper(nullValueMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT,
        imports = {
                MessageTemplates.ErrorsMessageTemplates.class,
                MessageTemplates.CommandsMessageTemplates.class,
                MessageTemplates.EventsMessageTemplates.class
        })
public interface PropertiesMapper {

    @Mapping(target = "scrapperClientUrl",
            defaultExpression = "java(ClientUrlProperties.DEFAULT_SCRAPPER_API)")
    ClientUrlProperties fillDefaults(ClientUrlProperties clientUrlProperties);

    @Mapping(target = "errors",
            defaultExpression = "java(fillDefaults(new ErrorsMessageTemplates()))")
    @Mapping(target = "commands",
            defaultExpression = "java(fillDefaults(new CommandsMessageTemplates()))")
    @Mapping(target = "events",
            defaultExpression = "java(fillDefaults(new EventsMessageTemplates()))")
    MessageTemplates fillDefaults(MessageTemplates messageTemplates);

    @Mapping(target = "unknownMessageTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_UNKNOWN_MESSAGE_TEMPLATE)")
    @Mapping(target = "unknownCommandTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_UNKNOWN_COMMAND_TEMPLATE)")
    @Mapping(target = "noLinkTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_NO_LINK_TEMPLATE)")
    @Mapping(target = "invalidLinkTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_INVALID_LINK_TEMPLATE)")
    @Mapping(target = "clientErrorTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_CLIENT_ERROR_TEMPLATE)")
    @Mapping(target = "errorTemplate",
            defaultExpression = "java(ErrorsMessageTemplates.DEFAULT_ERROR_TEMPLATE)")
    ErrorsMessageTemplates fillDefaults(ErrorsMessageTemplates errorsMessageTemplates);

    @Mapping(target = "startCommandTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_START_COMMAND_TEMPLATE)")
    @Mapping(target = "helpCommandTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_HELP_COMMAND_TEMPLATE)")
    @Mapping(target = "successTrackTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_SUCCESS_TRACK_TEMPLATE)")
    @Mapping(target = "successUntrackTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_SUCCESS_UNTRACK_TEMPLATE)")
    @Mapping(target = "listCommandTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_LIST_COMMAND_TEMPLATE)")
    @Mapping(target = "emptyListCommandTemplate",
            defaultExpression = "java(CommandsMessageTemplates.DEFAULT_EMPTY_LIST_COMMAND_TEMPLATE)")
    CommandsMessageTemplates fillDefaults(CommandsMessageTemplates commandsMessageTemplates);

    @Mapping(target = "updateNotificationTemplate",
            defaultExpression = "java(EventsMessageTemplates.DEFAULT_UPDATE_NOTIFICATION_TEMPLATE)")
    @Mapping(target = "branchesCountIncNotificationTemplate",
            defaultExpression = "java(EventsMessageTemplates.DEFAULT_BRANCHES_INC_NOTIFICATION_TEMPLATE)")
    @Mapping(target = "branchesCountDecNotificationTemplate",
            defaultExpression = "java(EventsMessageTemplates.DEFAULT_BRANCHES_DEC_NOTIFICATION_TEMPLATE)")
    @Mapping(target = "answersCountIncNotificationTemplate",
            defaultExpression = "java(EventsMessageTemplates.DEFAULT_ANSWERS_INC_NOTIFICATION_TEMPLATE)")
    EventsMessageTemplates fillDefaults(EventsMessageTemplates eventsMessageTemplates);

}