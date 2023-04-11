package ru.liga.service;

import lombok.RequiredArgsConstructor;
import ru.liga.model.Currency;
import ru.liga.model.Period;
import ru.liga.model.Rate;
import ru.liga.repository.RatesRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
/**
 * Класс отвечающий за работу алгоритма - "Мистический"
 */
@RequiredArgsConstructor
public class MysticAlgorithmForecastServiceImpl implements ForecastService {

    private final RatesRepository repository;

    private final int ALL_NUMBERS_OF_DAYS_IN_ALL_YEARS = 6570;
    private final int FIRST_YEAR_FOR_RATE = 2005;

    @Override
    public List<Rate> getRates(Currency currency, Period period) {
        List<Rate> rates = repository.getRates(currency, ALL_NUMBERS_OF_DAYS_IN_ALL_YEARS);
        List<Rate> resultRates = new ArrayList<>();
        if (period.isPeriod()) {
            LocalDate lastDate = LocalDate.now();
            while (!lastDate.equals(period.getDate())) {
                resultRates.add(getDateRate(rates, lastDate.plusDays(1), currency));
                lastDate = lastDate.plusDays(1);
            }
        } else {
            resultRates.add(getDateRate(rates, period.getDate(), currency));
        }
        return resultRates;
    }

    private Rate getDateRate(List<Rate> rates, LocalDate date, Currency currency) {
        List<LocalDate> previousYears = new ArrayList<>();
        int year = date.getYear();
        while (year >= FIRST_YEAR_FOR_RATE) {
            previousYears.add(LocalDate.of(year, date.getMonth(), date.getDayOfMonth()));
            year--;
        }
        List<Rate> filteredRates = rates.stream()
                .filter(rate -> rate.getDate().getMonth() == date.getMonth() && rate.getDate().getDayOfMonth() == date.getDayOfMonth()
                        && rate.getCurrency() == currency)
                .collect(Collectors.toList());
        if (filteredRates.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(previousYears.size());
        LocalDate randomDate = previousYears.get(index);
        Optional<Rate> optionalRate = Optional.of(filteredRates.stream()
                .filter((Rate rate) -> rate.getDate().isEqual(randomDate))
                .findFirst()
                .get());
        if (!optionalRate.isPresent()) {
            return null;
        }
        return new Rate(date, optionalRate.get().getRate(), currency);
    }
}
