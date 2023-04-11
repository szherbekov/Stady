package ru.liga.validator;

import ru.liga.exception.*;
import ru.liga.model.Algorithm;
import ru.liga.model.Currency;
import ru.liga.model.Output;
import ru.liga.model.Period;
import ru.liga.model.command.CommandNameEnum;
import ru.liga.util.DateTimeConstants;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static ru.liga.exception.ExceptionMessage.*;
import static ru.liga.model.command.CommandParameters.*;

/**
 * Класс отвечающий за валидацию
 */
public class CommandValidator {
    public Output getValidateOutput(Map<String, String> splitCommand) {
        Output output;
        if (splitCommand.containsKey(OUTPUT)) {
            try {
                output = Output.valueOf(splitCommand.get(OUTPUT).toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalOutputException(ILLEGAL_OUTPUT);
            }
        } else {
            output = Output.LIST;
        }
        return output;
    }

    public Algorithm getValidateAlgorithm(Map<String, String> splitCommand) {
        Algorithm algorithm;
        try {
            algorithm = Algorithm.valueOf(splitCommand.get(ALGORITHM).toUpperCase());
        } catch (Exception e) {
            throw new IllegalAlgorithmException(ILLEGAL_ALGORITHM);
        }
        return algorithm;
    }

    public CommandNameEnum getValidateCommandName(String name) {
        CommandNameEnum commandName;
        try {
            commandName = CommandNameEnum.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException(INVALID_COMMAND);
        }
        return commandName;
    }

    public List<Currency> getValidateCurrencies(String currenciesString) {
        List<String> splitCurrencies = Arrays.stream(currenciesString.split(",")).distinct().toList();
        List<Currency> currencies = new ArrayList<>();

        try {
            for (String splitCurrency : splitCurrencies) {
                currencies.add(Currency.valueOf(splitCurrency.toUpperCase()));
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalCurrencyTitleException(ILLEGAL_CURRENCY);
        }
        return currencies;
    }

    public Period getValidatePeriod(Map<String, String> splitCommand) {
        Period period = new Period();

        if (splitCommand.containsKey(PERIOD) && splitCommand.containsKey(DATE))
            throw new InvalidCommandException(INVALID_COMMAND);

        if (splitCommand.containsKey(PERIOD)) {
            switch (splitCommand.get(PERIOD).toLowerCase()) {
                case MONTH -> {
                    period.setDate(LocalDate.now().plusDays(30));
                    period.setPeriod(true);
                }
                case WEEK -> {
                    period.setDate(LocalDate.now().plusDays(7));
                    period.setPeriod(true);
                }
                default -> throw new IllegalPeriodException(ILLEGAL_PERIOD);
            }
        } else if (splitCommand.containsKey(DATE)) {
            if (splitCommand.get(DATE).equalsIgnoreCase(TOMORROW)) {
                period.setDate(LocalDate.now().plusDays(1));
                period.setPeriod(false);
            } else {
                try {
                    period.setDate(LocalDate.parse(splitCommand.get(DATE), DateTimeConstants.PARSE_DATE_FORMATTER_FROM_CSV));
                    period.setPeriod(false);
                } catch (Exception e) {
                    throw new IllegalDateException(ILLEGAL_DATE);
                }
            }
        } else {
            throw new InvalidCommandException(INVALID_COMMAND);
        }
        return period;
    }
}
