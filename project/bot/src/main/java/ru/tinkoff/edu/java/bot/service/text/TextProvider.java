package ru.tinkoff.edu.java.bot.service.text;

import com.pengrad.telegrambot.model.BotCommand;
import ru.tinkoff.edu.java.bot.model.dto.request.LinkEvent;

import java.util.List;

public class TextProvider {

    private static final String NO_LINK_ERROR_TEXT = "После команды через пробел нужно указать ссылку, которую нужно отследить";
    private static final String INVALID_LINK_TEXT = "Ссылка указана неверно";

    public static class BotTextProvider {

        public static String buildUnknownMessageText() {
            return "Извини, бот умеет общаться только через известные ему команды\n" +
                    "Для просмотра введи /help";
        }

        public static String buildUnknownCommandText(String command) {
            return String.format("Неизвестная команда: %s", command);
        }
    }

    public static class StartTextProvider {

        public static String buildStartMessage() {
            return """
                        Привет! Добро пожаловать!
                        Link Monitoring Bot - бот, который умеет отслеживать ссылки
                        Бот поддерживает два вида ссылок:
                        - GitHub (пример: https://github.com/sevolchenko/edu-tinkoff)
                        - StackOverflow (пример: https://stackoverflow.com/questions/10604298/spring-component-versus-bean)
                        Чтобы просмотреть команды бота, введи /help.
                        """;
        }

    }

    public static class HelpTextProvider {

        public static String buildHelpText(List<BotCommand> botCommands) {
            StringBuilder sb = new StringBuilder("Вот список доступных команд: ");

            botCommands.forEach(command -> {
                sb.append("\n");
                sb.append(command.command());
                sb.append(" - ");
                sb.append(command.description());
            });

            return sb.toString();
        }

    }

    public static class TrackTextProvider {

        public static String buildNoLinkErrorText() {
            return NO_LINK_ERROR_TEXT;
        }

        public static String buildSuccessfullyAddedLinkText(String link) {
            return String.format("Ссылка %s успешно добавлена к отслеживаемым!\n" +
                    "Мы пришлем уведомление, когда по адресу произойдут изменения :)", link);
        }

        public static String buildInvalidLinkText() {
            return INVALID_LINK_TEXT;
        }

    }

    public static class UntrackTextProvider {

        public static String buildNoLinkErrorText() {
            return NO_LINK_ERROR_TEXT;
        }

        public static String buildSuccessfullyRemovedLinkText(String link) {
            return String.format("Ссылка %s успешно удалена из отслеживаемых!\n" +
                    "Мы больше не пришлем уведомление :(", link);
        }

        public static String buildInvalidLinkText() {
            return INVALID_LINK_TEXT;
        }

    }

    public static class ListTextProvider {

        public static String buildEmptyLinksText() {
            return "Ты еще не отслеживаешь ни одной ссылки";
        }

        public static String buildLinksListText(List<String> links) {
            StringBuilder sb = new StringBuilder("Вот список ссылок, которые ты отслеживаешь:");

            links.forEach(link -> {
                sb.append("\n- ");
                sb.append(link);
            });

            return sb.toString();
        }

    }

    public static class NotificationTextProvider {
        public static String buildLinksListText(LinkEvent event, String link) {
            switch (event) {
                case UPDATED, default -> {
                    return String.format("Что-то новое по ссылке %s, пора проверить!", link);
                }
                case BRANCHES_COUNT_INCREASED -> {
                    return String.format("Создана новая ветка в репозитории %s, пора проверить!", link);
                }
                case BRANCHES_COUNT_DECREASED -> {
                    return String.format("Удалили ветку в репозитории %s, пора посмотреть!", link);
                }
                case ANSWERS_COUNT_INCREASED -> {
                    return String.format("Новый ответ на вопрос %s! Пора прочитать!", link);
                }
            }
        }
    }

}
