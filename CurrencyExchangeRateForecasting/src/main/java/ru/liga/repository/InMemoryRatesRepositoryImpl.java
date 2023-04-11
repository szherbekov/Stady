package ru.liga.repository;

import lombok.extern.slf4j.Slf4j;
import ru.liga.exception.CsvParseException;
import ru.liga.model.Currency;
import ru.liga.model.Rate;
import ru.liga.util.ParseRateCsv;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.liga.exception.ExceptionMessage.CSV_PARSE_ERROR;

/**
 * Инмемори репозиторий для хранения значений курсов валют, загруженных из CSV файлов и работы с этими данными
 */

@Slf4j
public class InMemoryRatesRepositoryImpl implements RatesRepository {

    private static final Map<Currency, List<Rate>> repository = new HashMap<>();


    static {
        try {
            repository.put(Currency.USD, ParseRateCsv.readCsvParseLinesToRatesList("/USD.csv"));
            repository.put(Currency.EUR, ParseRateCsv.readCsvParseLinesToRatesList("/EUR.csv"));
            repository.put(Currency.TRY, ParseRateCsv.readCsvParseLinesToRatesList("/TRY.csv"));
            repository.put(Currency.AMD, ParseRateCsv.readCsvParseLinesToRatesList("/AMD.csv"));
            repository.put(Currency.BGN, ParseRateCsv.readCsvParseLinesToRatesList("/BGN.csv"));
        } catch (Exception e) {
            throw new CsvParseException(CSV_PARSE_ERROR);
        }
        log.debug("Информация из CSV файлов успешно загружена в InMemoryRatesRepository");

    }

    @Override
    public List<Rate> getAllRates(Currency currency) {
        return repository.get(currency);
    }

    @Override
    public List<Rate> getRates(Currency currency, int numberOfDays) {
        return getAllRates(currency).stream()
                .limit(numberOfDays)
                .sorted(Comparator.comparing(Rate::getDate))
                .collect(Collectors.toList());
    }
}
