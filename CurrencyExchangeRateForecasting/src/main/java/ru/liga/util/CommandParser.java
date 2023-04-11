package ru.liga.util;

import ru.liga.exception.InvalidCommandException;
import ru.liga.exception.RepeatCommandParameterException;

import java.util.HashMap;
import java.util.Map;

import static ru.liga.exception.ExceptionMessage.INVALID_COMMAND;
import static ru.liga.exception.ExceptionMessage.REPEAT_COMMAND_PARAMETER;
import static ru.liga.model.command.CommandParameters.COMMAND_NAME;
import static ru.liga.model.command.CommandParameters.CURRENCIES;
/**
 * Класс для парсинга команд
 */
public class CommandParser {
    public Map<String, String> parse(String text) {
        if (text.isEmpty()) {
            throw new InvalidCommandException(INVALID_COMMAND);
        }

        String[] splitText = text.split(" ");
        if (splitText.length % 2 != 0 && splitText.length != 1) {
            throw new InvalidCommandException(INVALID_COMMAND);
        }
        Map<String, String> parseCommand = new HashMap<>();
        parseCommand.put(COMMAND_NAME, splitText[0]);
        if (splitText.length > 2) {
            parseCommand.put(CURRENCIES, splitText[1]);
            String checkRepeat = null;
            for (int i = 2; i < splitText.length; i++) {
                if (splitText[i].startsWith("-")) {
                    checkRepeat = parseCommand.putIfAbsent(splitText[i], splitText[i + 1]);
                }
                if (checkRepeat != null) {
                    throw new RepeatCommandParameterException(REPEAT_COMMAND_PARAMETER + splitText[i]);
                }
            }
        }
        return parseCommand;
    }
}
