package ru.tinkoff.edu.java.bot.configuration.properties;


import org.springframework.boot.context.properties.bind.ConstructorBinding;

public record MessageTemplates(

        ErrorsMessageTemplates errors,

        CommandsMessageTemplates commands,

        EventsMessageTemplates events

) {

    public MessageTemplates() {
        this(new ErrorsMessageTemplates(), new CommandsMessageTemplates(), new EventsMessageTemplates());
    }

    @ConstructorBinding
    public MessageTemplates {
    }

    public record ErrorsMessageTemplates(
            String unknownMessageTemplate,
            String unknownCommandTemplate,
            String noLinkTemplate,
            String invalidLinkTemplate,
            String clientErrorTemplate,
            String errorTemplate

    ) {

        public ErrorsMessageTemplates() {
            this(null, null, null, null, null, null);
        }

        @ConstructorBinding
        public ErrorsMessageTemplates {
        }

        public static final String DEFAULT_UNKNOWN_MESSAGE_TEMPLATE = """
                Извини, бот умеет общаться только через известные ему команды
                Для просмотра введи /help
                """;
        public static final String DEFAULT_UNKNOWN_COMMAND_TEMPLATE = """
                Неизвестная команда: %s
                """;
        public static final String DEFAULT_NO_LINK_TEMPLATE = """
                После команды через пробел нужно указать ссылку, которую нужно отследить
                """;
        public static final String DEFAULT_INVALID_LINK_TEMPLATE = """
                Ссылка указана неверно
                """;
        public static final String DEFAULT_CLIENT_ERROR_TEMPLATE = """
                Ссылка указана неверно
                """;
        public static final String DEFAULT_ERROR_TEMPLATE = """
                Произошла ошибка, попробуйте еще раз...
                """;

    }

    public record CommandsMessageTemplates(
            String startCommandTemplate,
            String helpCommandTemplate,
            String successTrackTemplate,
            String successUntrackTemplate,
            String listCommandTemplate,
            String emptyListCommandTemplate

    ) {

        public CommandsMessageTemplates() {
            this(null, null, null, null, null, null);
        }

        @ConstructorBinding
        public CommandsMessageTemplates {
        }

        public static final String DEFAULT_START_COMMAND_TEMPLATE = """
                Привет! Добро пожаловать!
                Link Monitoring Bot - бот, который умеет отслеживать ссылки
                Бот поддерживает два вида ссылок:
                - GitHub (пример: https://github.com/sevolchenko/edu-tinkoff)
                - StackOverflow (пример: https://stackoverflow.com/questions/10604298/spring-component-versus-bean)
                Чтобы просмотреть команды бота, введи /help.
                """;
        public static final String DEFAULT_HELP_COMMAND_TEMPLATE = """
                Вот список доступных команд:
                %s
                """;
        public static final String DEFAULT_SUCCESS_TRACK_TEMPLATE = """
                Ссылка %s успешно добавлена к отслеживаемым!
                Мы пришлем уведомление, когда по адресу произойдут изменения :)
                """;
        public static final String DEFAULT_SUCCESS_UNTRACK_TEMPLATE = """
                Ссылка %s успешно удалена из отслеживаемых!
                Мы больше не пришлем уведомление :(
                """;
        public static final String DEFAULT_LIST_COMMAND_TEMPLATE = """
                Вот список ссылок, которые ты отслеживаешь:
                %s
                """;
        public static final String DEFAULT_EMPTY_LIST_COMMAND_TEMPLATE = """
                Ты еще не отслеживаешь ни одной ссылки
                """;

    }

    public record EventsMessageTemplates(
            String updateNotificationTemplate,
            String branchesCountIncNotificationTemplate,
            String branchesCountDecNotificationTemplate,
            String answersCountIncNotificationTemplate
    ) {

        public EventsMessageTemplates() {
            this(null, null, null, null);
        }

        @ConstructorBinding
        public EventsMessageTemplates {
        }

        public static final String DEFAULT_UPDATE_NOTIFICATION_TEMPLATE = """
                Что-то новое по ссылке %s, пора проверить!
                """;
        public static final String DEFAULT_BRANCHES_INC_NOTIFICATION_TEMPLATE = """
                Создана новая ветка в репозитории %s, пора проверить!
                """;
        public static final String DEFAULT_BRANCHES_DEC_NOTIFICATION_TEMPLATE = """
                Удалили ветку в репозитории %s, пора посмотреть!
                """;
        public static final String DEFAULT_ANSWERS_INC_NOTIFICATION_TEMPLATE = """
                Новый ответ на вопрос %s! Пора прочитать!
                """;

    }

}
