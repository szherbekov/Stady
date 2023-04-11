package ru.liga.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import ru.liga.exception.ExceptionMessage;
import ru.liga.exception.IllegalDateException;
import ru.liga.model.Currency;
import ru.liga.model.Period;
import ru.liga.model.Rate;
import ru.liga.repository.RatesRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс отвечающий за работу алгоритма - "Прошлогодний"
 */
@AllArgsConstructor
public class LastYearAlgorithmForecastServiceImpl implements ForecastService {
    @NonNull
    private final RatesRepository repository;
    private final int THREE_YEARS = 1095;

    @Override
    public List<Rate> getRates(Currency currency, Period period) {

        List<Rate> rates = repository.getRates(currency, THREE_YEARS);
        if (period.getDate().isAfter(rates.get(rates.size() - 1).getDate().plusYears(2))) {
            throw new IllegalDateException(ExceptionMessage.ILLEGAL_DATE_FROM_ACTUAL + rates.get(rates.size() - 1).getDate());
        }
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
        BigDecimal dateRateOneYearBefore = rates.stream()
                .filter(rate -> date.minusYears(1).equals(rate.getDate()))
                .findFirst()
                .get()
                .getRate();
        if (date == null) {
            dateRateOneYearBefore = rates.stream()
                    .filter(rate -> date.minusYears(1).minusDays(1).equals(rate.getDate()))
                    .findFirst()
                    .get()
                    .getRate();
        }
        return new Rate(date, dateRateOneYearBefore, currency);
    }
}

